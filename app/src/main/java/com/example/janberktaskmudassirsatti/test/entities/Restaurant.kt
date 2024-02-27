package com.example.janberktaskmudassirsatti.test.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "menu_table")
data class Menu(
    @PrimaryKey val menuId: Long,
    val menuName: String
)

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey val catId: Long,
    val menuId: Long,
    val categoryName: String
)

@Entity(tableName = "allergens_table")
data class Allergen(
    @PrimaryKey val allergenId: Long,
    val menuId: Long,
    val categoryId: Long,
    val allergenName: String
)

@Entity(tableName = "tags_table")
data class Tag(
    @PrimaryKey val tagsId: Long,
    val allergenId: Long,
    val menuId: Long,
    val categoryId: Long,
    val tagName: String
)
