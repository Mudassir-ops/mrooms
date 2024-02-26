package com.example.janberktaskmudassirsatti.test.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.janberktaskmudassirsatti.test.entities.Allergen
import com.example.janberktaskmudassirsatti.test.entities.Menu


data class MenuWithAllergens(
    @Embedded val menu: Menu,
    @Relation(
        parentColumn = "id",
        entityColumn = "menuId"
    )
    val allergensWithTime: List<Allergen>
)


