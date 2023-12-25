package com.example.janberktaskmudassirsatti.di.repositories

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.janberktaskmudassirsatti.models.ImageModel
import com.example.janberktaskmudassirsatti.utill.AppConstants.DIRECTORY_NAME
import com.example.janberktaskmudassirsatti.utill.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.util.Date
import javax.inject.Inject

class ScreenshotRepositoryImpl @Inject constructor(
    private val context: Context,

    ) : ScreenshotRepository {


    /*** my method to extract all  screenshot from My Custom Folder */
    override fun fetchAllScreenshots(): Flow<DataState<List<ImageModel>>> = flow {
        val imagesList = mutableListOf<ImageModel>()
        val contentResolver: ContentResolver = context.contentResolver
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATA
        )
        val selection =
            "${MediaStore.Images.Media.MIME_TYPE} = ? AND ${MediaStore.Images.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("image/jpeg", "%/$DIRECTORY_NAME/%")
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val cursor = contentResolver.query(
            uri, projection, selection, selectionArgs, sortOrder
        )
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val displayName =
                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                val dateAdded =
                    it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED))
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                )
                val imagePath = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                imagesList.add(
                    ImageModel(
                        contentUri.toString(), imagePath, displayName, Date(dateAdded)
                    )
                )
            }
        }

        emit(DataState.Success(imagesList))
    }


    /** Here i saved images into gallery with media query in using Coroutine  */
    override suspend fun saveScreenShot(bitmap: Bitmap?): Flow<DataState<Boolean>> = flow {
        val result = saveImageToGallery(context = context, bitmap = bitmap ?: return@flow)
        if (result) {
            emit(DataState.Success(true))
        } else {
            emit(DataState.Success(false))
        }
    }

    override suspend fun deleteImage(imageUri: String): Flow<DataState<Boolean>> {
        TODO("Not yet implemented")
    }

    private suspend fun saveImageToGallery(
        context: Context, bitmap: Bitmap
    ): Boolean {
        return withContext(Dispatchers.IO) {
            var outputStream: OutputStream? = null
            try {
                val currentTimeMillis = System.currentTimeMillis()
                val title = "janberk_screenshot_$currentTimeMillis"
                val description = "Screenshot taken at ${
                    SimpleDateFormat.getDateTimeInstance().format(Date(currentTimeMillis))
                }"
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.TITLE, title)
                    put(MediaStore.Images.Media.DESCRIPTION, description)
                    put(MediaStore.Images.Media.DATE_ADDED, currentTimeMillis)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.Images.Media.DATE_TAKEN, currentTimeMillis)
                    }
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/$DIRECTORY_NAME"
                    )
                }
                val contentResolver = context.contentResolver
                val imageUri =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                outputStream = imageUri?.let { contentResolver.openOutputStream(it) }
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.close()
                    return@withContext true
                } else {
                    return@withContext false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            } finally {
                outputStream?.close()
            }
        }
    }


}