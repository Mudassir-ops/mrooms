package com.example.janberktaskmudassirsatti.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.janberktaskmudassirsatti.Appconstants.DataState
import com.example.janberktaskmudassirsatti.di.repository.ScreenshotRepository
import com.example.janberktaskmudassirsatti.models.ImageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val screenshotRepository: ScreenshotRepository) :
    ViewModel() {

    private val _screenShotData: MutableLiveData<DataState<List<ImageModel>>> = MutableLiveData()
    val screenShotDataState: LiveData<DataState<List<ImageModel>>>
        get() = _screenShotData

    init {
        fetchAllScreenShots()
    }

    private fun fetchAllScreenShots() {
        viewModelScope.launch {
            try {
                screenshotRepository.getAllImages()
                    .flowOn(Dispatchers.IO)
                    .collect { images ->
                        _screenShotData.value = images
                    }
            } catch (e: Exception) {
                _screenShotData.value = DataState.Error(e.message ?: "Unknown error")
            }

        }
    }
}