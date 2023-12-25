package com.example.janberktaskmudassirsatti.di.repository

import android.content.Context
import com.example.janberktaskmudassirsatti.Appconstants.DataState
import com.example.janberktaskmudassirsatti.models.ImageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class ScreenshotRepositoryImpl @Inject constructor(
    private val context: Context
) : ScreenshotRepository {

    private val mStoreDir: String =
        context.getExternalFilesDir(null)?.absolutePath + "/screenshots/"

    override fun getAllImages(): Flow<DataState<List<ImageModel>>> = flow {
        val imagesList = mutableListOf<ImageModel>()
        val imageFiles = File(mStoreDir).listFiles()
        imageFiles?.forEach { file ->
            imagesList.add(ImageModel(file.absolutePath))
        }
        emit(DataState.Success(imagesList))
    }

}