package com.example.trackit.ui.Nutrition

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

data class DailyLog(
    val date: LocalDate,
    var breakfastFoods: MutableList<FoodData>,
    var lunchFoods: MutableList<FoodData>,
    var dinnerFoods: MutableList<FoodData>,
    var snackFoods: MutableList<FoodData>,
    var totalProteins: Int = 0,
    var totalFats: Int = 0,
    var totalCarbs: Int = 0,
    var totalCalories: Int = 0
)

@Entity(tableName = "food_items")
data class FoodData(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val name: String,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val calories: Double,
    val gramsEntered: Int
)