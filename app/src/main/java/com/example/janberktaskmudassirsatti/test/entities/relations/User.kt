package com.example.janberktaskmudassirsatti.test.entities.relations

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey var id: String
)

@Entity
data class Pet(
    @PrimaryKey
    var id: String,
    var userId: String? = null,
    var petName: String? = null
)

@Entity
data class Category(
    @PrimaryKey
    var id: String,
    var petId: String? = null,
    var categoryName: String? = null
)

@Entity
data class SubCategory(
    @PrimaryKey
    var id: String,
    var catId: String? = null,
    var subCategoryName: String? = null
)
