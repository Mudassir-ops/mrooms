package com.example.janberktaskmudassirsatti.models

import java.util.Date

data class ImageModel(
    val contentUri: String,
    val imagePath: String,
    val displayName: String?,
    val dateAdded: Date?
)