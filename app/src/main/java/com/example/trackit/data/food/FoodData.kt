package com.example.trackit.ui.Nutrition

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodData(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val name: String,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val calories: Double,
    val gramsEntered: Int
)

