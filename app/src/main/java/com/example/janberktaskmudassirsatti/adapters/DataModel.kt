package com.example.janberktaskmudassirsatti.adapters

sealed class DataModel {
    data class TemplatesScreenshot(
        val title: String,
        val resId: Int
    ) : DataModel()

    data class TemplatesAd(
        val dataList: ArrayList<Int>
    ) : DataModel()
}