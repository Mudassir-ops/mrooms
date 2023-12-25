package com.example.janberktaskmudassirsatti.ui.activities

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.janberktaskmudassirsatti.R
import com.example.janberktaskmudassirsatti.di.repositories.ScreenshotRepository
import com.example.janberktaskmudassirsatti.utill.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenshotViewModel @Inject constructor(
    private val application: Application, private val screenshotRepository: ScreenshotRepository
) : ViewModel() {

    private val _isScreenshotSaved: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val isScreenshotSaved: LiveData<DataState<Boolean>>
        get() = _isScreenshotSaved

    fun saveScreenshot(bitmap: Bitmap?) {
        viewModelScope.launch {
            try {
                screenshotRepository.saveScreenShot(bitmap = bitmap).flowOn(Dispatchers.IO)
                    .collect { images ->
                        _isScreenshotSaved.value = images
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                _isScreenshotSaved.value =
                    DataState.Error(application.getString(R.string.screenshot_not_saved))
            }
        }
    }

}