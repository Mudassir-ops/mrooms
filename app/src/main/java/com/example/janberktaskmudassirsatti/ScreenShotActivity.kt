package com.example.janberktaskmudassirsatti


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
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
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.janberktaskmudassirsatti.databinding.ActivityScreenShotBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Method


class ScreenShotActivity : AppCompatActivity() {


    private val TAG = "ScreenCaptureService"
    private val RESULT_CODE = "RESULT_CODE"
    private val DATA = "DATA"
    private val ACTION = "ACTION"
    private val START = "START"
    private val STOP = "STOP"
    private val SCREENCAP_NAME = "screencap"

    private var IMAGES_PRODUCED = 0

    private var mMediaProjection: MediaProjection? = null
    private var mStoreDir: String? = null
    private var mImageReader: ImageReader? = null
    private val mHandler: Handler? = null
    private var mDisplay: Display? = null
    private var mVirtualDisplay: VirtualDisplay? = null
    private var mDensity = 0
    private var mWidth = 0
    private var mHeight = 0
    private var mRotation = 0
    private var mOrientationChangeCallback: OrientationChangeCallback? =
        null

    @SuppressLint("WrongConstant", "PrivateApi")
    fun setExpandNotificationDrawer(context: Context, expand: Boolean) {
        try {
            val statusBarService = context.getSystemService("statusbar")
            val methodName =
                if (expand)
                    "expandNotificationsPanel"
                else
                    "collapsePanels"
            val statusBarManager: Class<*> = Class.forName("android.app.StatusBarManager")
            val method: Method = statusBarManager.getMethod(methodName)
            method.invoke(statusBarService)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private val mediaProjectionManager: MediaProjectionManager by lazy {
        application.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    }
    private val activityResultLaunch: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            startProjection(result.resultCode, result.data!!)
        }

        val data: Intent? = result.data

        Log.e("MudassirSatti", ": Reuslt${data?.data}---${result.resultCode}")

    }


    private var binding: ActivityScreenShotBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions


        super.onCreate(savedInstanceState)
        binding = ActivityScreenShotBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //  setExpandNotificationDrawer(context =this@ScreenShotActivity, expand = false)

        activityResultLaunch.launch(mediaProjectionManager.createScreenCaptureIntent())

        binding?.apply {
            invisibleActivity.setOnClickListener {
                Toast.makeText(
                    this@ScreenShotActivity,
                    "Invivible Activty Disaly",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startProjection(resultCode: Int, data: Intent) {
        val mpManager =
            application.getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        if (mMediaProjection == null) {
            mMediaProjection = mpManager.getMediaProjection(resultCode, data)
            if (mMediaProjection != null) {
                // display metrics
                mDensity = Resources.getSystem().displayMetrics.densityDpi
                val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
                mDisplay = windowManager.defaultDisplay

                // create virtual display depending on device width / height
                createVirtualDisplay()

                // register orientation change callback
                mOrientationChangeCallback = OrientationChangeCallback(this)
                if (mOrientationChangeCallback!!.canDetectOrientation()) {
                    mOrientationChangeCallback!!.enable()
                }

                // register media projection stop callback
                mMediaProjection!!.registerCallback(MediaProjectionStopCallback(), mHandler)
            }
        }
    }

    private fun getVirtualDisplayFlags(): Int {
        return DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC
    }


    @SuppressLint("WrongConstant")
    private fun createVirtualDisplay() {
        mWidth = Resources.getSystem().displayMetrics.widthPixels
        mHeight = Resources.getSystem().displayMetrics.heightPixels
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2)
        mVirtualDisplay = mMediaProjection!!.createVirtualDisplay(
            SCREENCAP_NAME,
            mWidth,
            mHeight,
            mDensity,
            getVirtualDisplayFlags(),
            mImageReader!!.surface,
            null,
            mHandler
        )
        mImageReader!!.setOnImageAvailableListener(ImageAvailableListener(), mHandler)
    }

    private inner class ImageAvailableListener : OnImageAvailableListener {
        private var isCapturing = true
        override fun onImageAvailable(reader: ImageReader) {
            if (!isCapturing) {
                return
            }
            val externalFilesDir = getExternalFilesDir(null)
            if (externalFilesDir != null) {
                mStoreDir = externalFilesDir.absolutePath + "/screenshots/"
                val storeDirectory = mStoreDir?.let { File(it) }
                if (storeDirectory?.exists() != true) {
                    val success = storeDirectory?.mkdirs()
                    if (success != true) {
                        Log.e(TAG, "failed to create file storage directory.")
                    }
                }
            }

            var fos: FileOutputStream? = null
            var bitmap: Bitmap? = null
            try {
                mImageReader?.acquireLatestImage().use { image ->
                    if (image != null) {
                        val planes: Array<Plane> = image.planes
                        val buffer = planes[0].buffer
                        val pixelStride = planes[0].pixelStride
                        val rowStride = planes[0].rowStride
                        val rowPadding: Int = rowStride - pixelStride * mWidth

                        // create bitmap
                        bitmap = Bitmap.createBitmap(
                            mWidth + rowPadding / pixelStride,
                            mHeight,
                            Bitmap.Config.ARGB_8888
                        )
                        bitmap!!.copyPixelsFromBuffer(buffer)
                        fos = FileOutputStream("$mStoreDir/myscreen_$IMAGES_PRODUCED.png")
                        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, fos!!)
                        IMAGES_PRODUCED++
                        Log.e(
                            TAG,
                            "captured image: $IMAGES_PRODUCED"
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos!!.close()
                    } catch (ioe: IOException) {
                        ioe.printStackTrace()
                    }
                }
                if (bitmap != null) {
                    bitmap!!.recycle()
                }

                isCapturing = false
                stopCapturingImages()
            }
        }
    }


    private inner class OrientationChangeCallback internal constructor(context: Context?) :
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
            Log.e("mudasssirSatti", "stopping projection.")
            mHandler?.post(Runnable {
                mVirtualDisplay?.release()
                mImageReader?.setOnImageAvailableListener(null, null)
                mOrientationChangeCallback?.disable()
                mMediaProjection?.unregisterCallback(this@MediaProjectionStopCallback)
            })
        }
    }

    private fun stopCapturingImages() {
        mMediaProjection?.stop()
        finishAffinity()
        finish()
    }
}