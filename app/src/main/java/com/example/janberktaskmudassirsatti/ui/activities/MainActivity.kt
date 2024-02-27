package com.example.janberktaskmudassirsatti.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.janberktaskmudassirsatti.databinding.ActivityMainBinding
import com.example.janberktaskmudassirsatti.test.SchoolDatabase
import com.example.janberktaskmudassirsatti.test.entities.Menu
import com.example.janberktaskmudassirsatti.test.entities.relations.Category
import com.example.janberktaskmudassirsatti.test.entities.relations.Pet
import com.example.janberktaskmudassirsatti.test.entities.relations.SubCategory
import com.example.janberktaskmudassirsatti.test.entities.relations.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val dao = SchoolDatabase.getInstance(this).restaurantDao()


        binding?.apply {
            button.setOnClickListener {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {

                        dao.insertUser(User("mudassir"))
                        dao.insertPet(
                            Pet(
                                id = "petOne", userId = "mudassir", petName = "cat"

                            )
                        )
                        dao.insertCategory(
                            Category(
                                id = "catId", petId = "petOne", categoryName = "Saban"

                            )
                        )
                        dao.insertSubCategory(
                           SubCategory(id = "subCatId", catId = "catId", subCategoryName = "sub kutta")
                        )

                    }

                }
            }

            button2.setOnClickListener {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val reuslt = dao.loadUsersWithPets()
                        Log.e("ResultLIST---->", "onCreate: $reuslt")
                    }

                }
            }
        }

    }
}