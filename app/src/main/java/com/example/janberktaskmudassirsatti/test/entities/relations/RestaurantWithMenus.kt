package com.example.janberktaskmudassirsatti.test.entities.relations

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithPets(
    @Embedded var user: User? = null,

    @Relation(
        parentColumn = "id", entityColumn = "userId", entity = Pet::class
    ) var pets: List<PetWithCategories>? = null,

    @Relation(
        parentColumn = "id", entityColumn = "petId", entity = Category::class
    ) var categories: List<CategoriesWithSubCategories>? = null,

    @Relation(
        parentColumn = "id", entityColumn = "catId"
    ) var subCategories: List<SubCategory>? = null

)

data class UserWithPets1(
    @Embedded var user: User? = null, @Relation(
        parentColumn = "id", entityColumn = "userId", entity = Pet::class
    ) var pets: List<PetWithCategories>? = null
)

data class PetWithCategories(
    @Embedded var pet: Pet? = null, @Relation(
        parentColumn = "id", entityColumn = "petId", entity = Category::class
    ) var categories: List<CategoriesWithSubCategories>? = null
)

data class CategoriesWithSubCategories(
    @Embedded var category: Category? = null, @Relation(
        parentColumn = "id", entityColumn = "catId"
    ) var subCategories: List<SubCategory>? = null
)


//data class MenuWithCategoriesAndAllergens(
//    @Embedded val menu: Menu, @Relation(
//        parentColumn = "menuId", entityColumn = "menuId"
//    ) val categories: List<CategoryWithAllergens>
//)
//
//
//data class CategoryWithAllergens(
//    @Embedded val category: Category, @Relation(
//        parentColumn = "menuId", entityColumn = "menuId"
//    ) val allergens: List<AllergenWithTags>
//)
//
//data class AllergenWithTags(
//    @Embedded val allergen: Allergen, @Relation(
//        parentColumn = "menuId", entityColumn = "menuId"
//    ) val tags: List<Tag>
//)
