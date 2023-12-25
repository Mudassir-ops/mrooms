package com.example.janberktaskmudassirsatti.ui.fragments.viewscreenshoot

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.janberktaskmudassirsatti.di.repositories.ScreenshotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewScreenshotViewModel @Inject constructor(private val screenshotRepository: ScreenshotRepository) :
    ViewModel() {
    val screenShotTitle = ObservableField<String>()
    val imageURL = ObservableField<String>()
}