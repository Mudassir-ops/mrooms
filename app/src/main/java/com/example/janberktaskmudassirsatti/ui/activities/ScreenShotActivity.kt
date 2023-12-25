package com.example.janberktaskmudassirsatti.ui.activities


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.hardware.display.VirtualDisplay
import android.media.Image.Plane
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Display
import android.view.OrientationEventListener
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.janberktaskmudassirsatti.databinding.ActivityScreenShotBinding
import com.example.janberktaskmudassirsatti.utill.AllKotlinCallBacks.listener
import com.example.janberktaskmudassirsatti.utill.AppConstants.MEDIA_PROJECTION_SCREEN_NAME
import com.example.janberktaskmudassirsatti.utill.AppConstants.TAG
import com.example.janberktaskmudassirsatti.utill.DataState
import com.example.janberktaskmudassirsatti.utill.compressBitmapToByteArray
import com.example.janberktaskmudassirsatti.utill.getVirtualDisplayFlags
import com.example.janberktaskmudassirsatti.utill.stopCapturingImages
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ScreenShotActivity : AppCompatActivity() {

    private var mMediaProjection: MediaProjection? = null
    private var mImageReader: ImageReader? = null
    private val mHandler: Handler? = null
    private var mDisplay: Display? = null
    private var mVirtualDisplay: VirtualDisplay? = null
    private var mDensity = 0
    private var mWidth = 0
    private var mHeight = 0
    private var mRotation = 0
    private var mOrientationChangeCallback: OrientationChangeCallback? = null
    private val screenshotViewModel: ScreenshotViewModel by viewModels()
    private val mediaProjectionManager: MediaProjectionManager by lazy {
        application.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }
    private val activityResultLaunch: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            lifecycleScope.launch {
                /** If a start projection too quickly than minor dim effect appear in screenshot but with this delay my screenshot is clearly visible */
                delay(100)
                startProjection(result.resultCode, result.data ?: return@launch)
            }
        }
    }
    private var binding: ActivityScreenShotBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenShotBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        activityResultLaunch.launch(mediaProjectionManager.createScreenCaptureIntent())
        screenshotViewModel.isScreenshotSaved.observe(this) { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    listener?.invoke()  //----This is to update if our app open and user take screenshot from notification
                    stopCapturingImages(mediaProjection = mMediaProjection)

                }

                is DataState.Error -> {
                    stopCapturingImages(mediaProjection = mMediaProjection)
                }

                is DataState.Loading -> {
                    Log.e(TAG, "onCreate: Capturing")
                }

                else -> {
                    stopCapturingImages(mediaProjection = mMediaProjection)
                }
            }
        }
    }

    private fun startProjection(resultCode: Int, data: Intent) {
        val mpManager =
            application.getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        if (mMediaProjection == null) {
            mMediaProjection = mpManager.getMediaProjection(resultCode, data)
            if (mMediaProjection != null) {
                mDensity = Resources.getSystem().displayMetrics.densityDpi
                val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
                @Suppress("DEPRECATION")
                mDisplay = windowManager.defaultDisplay
                createVirtualDisplay()
                mOrientationChangeCallback = OrientationChangeCallback(this)
                if (mOrientationChangeCallback?.canDetectOrientation() == true) {
                    mOrientationChangeCallback?.enable()
                }
                mMediaProjection?.registerCallback(MediaProjectionStopCallback(), mHandler)
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun createVirtualDisplay() {
        mWidth = Resources.getSystem().displayMetrics.widthPixels
        mHeight = Resources.getSystem().displayMetrics.heightPixels
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2)
        mVirtualDisplay = mMediaProjection?.createVirtualDisplay(
            MEDIA_PROJECTION_SCREEN_NAME,
            mWidth,
            mHeight,
            mDensity,
            getVirtualDisplayFlags(),
            mImageReader?.surface,
            null,
            mHandler
        )
        mImageReader?.setOnImageAvailableListener(ImageAvailableListener(), mHandler)
    }

    private inner class ImageAvailableListener : OnImageAvailableListener {
        private var isCapturing = true
        override fun onImageAvailable(reader: ImageReader) {
            if (!isCapturing) {
                return
            }
            var bitmap: Bitmap? = null
            try {
                mImageReader?.acquireLatestImage().use { image ->
                    if (image != null) {
                        val planes: Array<Plane> = image.planes
                        val buffer = planes[0].buffer
                        val pixelStride = planes[0].pixelStride
                        val rowStride = planes[0].rowStride
                        val rowPadding: Int = rowStride - pixelStride * mWidth
                        bitmap = Bitmap.createBitmap(
                            mWidth + rowPadding / pixelStride,
                            mHeight,
                            Bitmap.Config.ARGB_8888
                        )
                        bitmap?.copyPixelsFromBuffer(buffer)
                        val compressedBitmapByteArray = bitmap?.compressBitmapToByteArray()
                        val compressedBitmap = compressedBitmapByteArray?.size?.let {
                            BitmapFactory.decodeByteArray(
                                compressedBitmapByteArray, 0,
                                it
                            )
                        }
                        screenshotViewModel.saveScreenshot(compressedBitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (bitmap != null) {
                    bitmap?.recycle()
                }
                isCapturing = false
            }
        }
    }

    private inner class OrientationChangeCallback(context: Context?) :
        OrientationEventListener(context) {
        override fun onOrientationChanged(orientation: Int) {
            val rotation: Int? = mDisplay?.rotation
            if (rotation != mRotation) {
                if (rotation != null) {
                    mRotation = rotation
                }
                try {
                    mVirtualDisplay?.release()
                    mImageReader?.setOnImageAvailableListener(null, null)
                    createVirtualDisplay()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private inner class MediaProjectionStopCallback : MediaProjection.Callback() {
        override fun onStop() {
            mHandler?.post {
                mVirtualDisplay?.release()
                mImageReader?.setOnImageAvailableListener(null, null)
                mOrientationChangeCallback?.disable()
                mMediaProjection?.unregisterCallback(this@MediaProjectionStopCallback)
            }
        }
    }

}