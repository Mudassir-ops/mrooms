package com.example.janberktaskmudassirsatti.di.repository

import com.example.janberktaskmudassirsatti.Appconstants.DataState
import com.example.janberktaskmudassirsatti.models.ImageModel
import kotlinx.coroutines.flow.Flow


interface ScreenshotRepository {
    fun getAllImages(): Flow<DataState<List<ImageModel>>>
}