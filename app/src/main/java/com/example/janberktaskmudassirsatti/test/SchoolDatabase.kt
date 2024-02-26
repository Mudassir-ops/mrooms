package com.example.janberktaskmudassirsatti.test

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.janberktaskmudassirsatti.test.entities.Allergen
import com.example.janberktaskmudassirsatti.test.entities.Category
import com.example.janberktaskmudassirsatti.test.entities.Menu
import com.example.janberktaskmudassirsatti.test.entities.Restaurant
import com.example.janberktaskmudassirsatti.test.entities.Tag

@Database(
    entities = [Restaurant::class, Menu::class, Category::class, Tag::class, Allergen::class],
    version = 1
)
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