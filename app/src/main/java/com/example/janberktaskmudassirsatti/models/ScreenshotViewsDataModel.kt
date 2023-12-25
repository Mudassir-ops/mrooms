package com.example.janberktaskmudassirsatti.models

sealed class ScreenshotViewsDataModel {
    data class TemplatesScreenshot(
        val contentUri: String,
        val imagePath: String,
        val displayName: String?
    ) : ScreenshotViewsDataModel()

    data class TemplatesAd(
        val resId: Int = 0
    ) : ScreenshotViewsDataModel()
}