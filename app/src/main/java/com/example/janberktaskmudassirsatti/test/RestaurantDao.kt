package com.example.janberktaskmudassirsatti.test

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.janberktaskmudassirsatti.test.entities.Allergen
import com.example.janberktaskmudassirsatti.test.entities.Category
import com.example.janberktaskmudassirsatti.test.entities.Menu
import com.example.janberktaskmudassirsatti.test.entities.Restaurant
import com.example.janberktaskmudassirsatti.test.entities.Tag
import com.example.janberktaskmudassirsatti.test.entities.relations.MenuWithAllergens
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: Restaurant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: Menu)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllergen(allergen: Allergen)

    @Transaction
    @Query("SELECT * FROM Menu WHERE id = :menuId")
    fun getMenuWithAllergens(menuId: Long): Flow<MenuWithAllergens>

}
