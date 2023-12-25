package com.example.janberktaskmudassirsatti.di.repositories

import android.graphics.Bitmap
import com.example.janberktaskmudassirsatti.models.ImageModel
import com.example.janberktaskmudassirsatti.utill.DataState
import kotlinx.coroutines.flow.Flow


interface ScreenshotRepository {
    fun fetchAllScreenshots(): Flow<DataState<List<ImageModel>>>
    suspend fun saveScreenShot(bitmap: Bitmap?): Flow<DataState<Boolean>>
}