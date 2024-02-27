package com.example.janberktaskmudassirsatti.test.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromCategoryList(categoryList: List<Category>?): String? {
        return Gson().toJson(categoryList)
    }

    @TypeConverter
    fun toCategoryList(categoryListJson: String?): List<Category>? {
        val type = object : TypeToken<List<Category>>() {}.type
        return Gson().fromJson(categoryListJson, type)
    }

    @TypeConverter
    fun fromAllergenList(allergenList: List<Allergen>?): String? {
        return Gson().toJson(allergenList)
    }

    @TypeConverter
    fun toAllergenList(allergenListJson: String?): List<Allergen>? {
        val type = object : TypeToken<List<Allergen>>() {}.type
        return Gson().fromJson(allergenListJson, type)
    }

    @TypeConverter
    fun fromTagList(tagList: List<Tag>?): String? {
        return Gson().toJson(tagList)
    }

    @TypeConverter
    fun toTagList(tagListJson: String?): List<Tag>? {
        val type = object : TypeToken<List<Tag>>() {}.type
        return Gson().fromJson(tagListJson, type)
    }

}
