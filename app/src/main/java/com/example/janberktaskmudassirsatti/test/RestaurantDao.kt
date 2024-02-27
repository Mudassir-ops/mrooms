package com.example.janberktaskmudassirsatti.test

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.janberktaskmudassirsatti.test.entities.Allergen
import com.example.janberktaskmudassirsatti.test.entities.Menu
import com.example.janberktaskmudassirsatti.test.entities.relations.Pet
import com.example.janberktaskmudassirsatti.test.entities.relations.SubCategory
import com.example.janberktaskmudassirsatti.test.entities.relations.User
import com.example.janberktaskmudassirsatti.test.entities.relations.UserWithPets
import com.example.janberktaskmudassirsatti.test.entities.relations.UserWithPets1

@Dao
interface RestaurantDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: Menu)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: com.example.janberktaskmudassirsatti.test.entities.Tag)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllergen(allergen: Allergen)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: com.example.janberktaskmudassirsatti.test.entities.relations.Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategory(subCategory: SubCategory)


    @Query("SELECT * FROM User")
    suspend fun loadUsersWithPets(): List<UserWithPets?>?


//    @Transaction
//    @Query("SELECT * FROM menu_table")
//    fun getRestaurantsWithMenus(): Flow<List<MenuWithCategoriesAndAllergens>>

}
