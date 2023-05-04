package com.example.trackit.ui.Nutrition

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class FoodData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val calories: Double
)

enum class ExpandedPanel { Breakfast, Lunch, Dinner, Snack }

