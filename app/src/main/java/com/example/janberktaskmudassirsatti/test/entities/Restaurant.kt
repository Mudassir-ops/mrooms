package com.example.janberktaskmudassirsatti.test.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Restaurant(
    @PrimaryKey val id: Long,
    val name: String,
    val address: String,
    val phoneNo: String,
    val status: String,
    val cuisineType: String,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
    val updatedAt: String
)

@Entity
data class Menu(
    @PrimaryKey val id: Long,
    val restaurantId: Long,
    val menuName: String,
    val posButtonColor: String,
    val menuDescription: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

@Entity
data class Category(
    @PrimaryKey val id: Long,
    val menuId: Long,
    val categoryName: String,
    val color: String,
    val image: String,
    val showOnMenu: Boolean,
    val sortOrder: Int,
    val startTime: String,
    val endTime: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

@Entity
data class Tag(
    @PrimaryKey val id: Long,
    val tagName: String
)



@Entity
data class Allergen(
    @PrimaryKey val id: Long,
    val allergenName: String,
    val menuId: Long,
)

@Entity
data class Time(
    @PrimaryKey val timeId: Long,
    val time: String
)