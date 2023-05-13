package com.example.trackit.ui.Nutrition

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.trackit.data.food.Globals
import java.time.LocalDate

@Entity(tableName = "food_items")
data class FoodData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val calories: Double,
    val gramsEntered: Int
)

