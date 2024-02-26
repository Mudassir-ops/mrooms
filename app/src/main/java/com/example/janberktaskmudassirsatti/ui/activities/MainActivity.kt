package com.example.janberktaskmudassirsatti.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.janberktaskmudassirsatti.databinding.ActivityMainBinding
import com.example.janberktaskmudassirsatti.test.SchoolDatabase
import com.example.janberktaskmudassirsatti.test.entities.Allergen
import com.example.janberktaskmudassirsatti.test.entities.Category
import com.example.janberktaskmudassirsatti.test.entities.Menu
import com.example.janberktaskmudassirsatti.test.entities.Restaurant
import com.example.janberktaskmudassirsatti.test.entities.Tag
import com.example.janberktaskmudassirsatti.test.entities.Time
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val dao = SchoolDatabase.getInstance(this).restaurantDao()

        lifecycleScope.launch {

            dao.insertRestaurant(
                restaurant = Restaurant(
                    id = 1,
                    name = "Queta Cafe",
                    address = "morgah",
                    phoneNo = "03125307585",
                    status = "open",
                    cuisineType = "sjsj",
                    latitude = 0.0,
                    longitude = 0.0,
                    createdAt = "2727-29-2020",
                    updatedAt = "22-292-92"
                )
            )

            dao.insertCategory(
                category = Category(
                    id = 8020,
                    menuId = 9097,
                    categoryName = "Josephine Pittman",
                    color = "wisi",
                    image = "dicunt",
                    showOnMenu = false,
                    sortOrder = 1194,
                    startTime = "meliore",
                    endTime = "expetendis",
                    status = "taciti",
                    createdAt = "tantas",
                    updatedAt = "blandit"
                )
            )
            dao.insertAllergen(
                allergen = Allergen(
                    id = 7892,
                    allergenName = "Victoria Oneal", menuId = 9130
                )
            )
            dao.insertTag(
                tag = Tag(
                    id = 1979,
                    tagName = "Mohammad O'Donnell"
                )
            )

            dao.insertMenu(
                menu = Menu(
                    id = 9130,
                    restaurantId = 4185,
                    menuName = "Isaac Davidson",
                    posButtonColor = "varius",
                    menuDescription = "eam",
                    status = "venenatis",
                    createdAt = "patrioque",
                    updatedAt = "enim"
                )
            )
            dao.getMenuWithAllergens(menuId = 9130).collect { menuWithAllergens ->
                println("Menu: ${menuWithAllergens.menu}")
            }
        }
    }

}