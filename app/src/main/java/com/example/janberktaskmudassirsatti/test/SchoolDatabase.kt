package com.example.janberktaskmudassirsatti.test

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.janberktaskmudassirsatti.test.entities.Allergen
import com.example.janberktaskmudassirsatti.test.entities.Category
import com.example.janberktaskmudassirsatti.test.entities.Converters
import com.example.janberktaskmudassirsatti.test.entities.Menu
import com.example.janberktaskmudassirsatti.test.entities.Tag
import com.example.janberktaskmudassirsatti.test.entities.relations.Pet
import com.example.janberktaskmudassirsatti.test.entities.relations.SubCategory
import com.example.janberktaskmudassirsatti.test.entities.relations.User

@Database(
    entities = [Menu::class, Category::class, Allergen::class, Tag::class, User::class, com.example.janberktaskmudassirsatti.test.entities.relations.Category::class, Pet::class, SubCategory::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class SchoolDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: SchoolDatabase? = null

        fun getInstance(context: Context): SchoolDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, SchoolDatabase::class.java, "school_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}