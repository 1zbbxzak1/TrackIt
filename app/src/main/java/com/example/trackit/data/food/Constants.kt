package com.example.trackit.data.food

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.trackit.ui.Nutrition.FoodData
import kotlin.math.roundToInt

object Globals {
    var TotalProteins by mutableStateOf(0)
    var TotalFats by mutableStateOf(0)
    var TotalCarbs by mutableStateOf(0)
    var TotalCalories by mutableStateOf(0)
}

object ListFood {
    var breakfastFoods = mutableStateListOf<FoodData>()
    var lunchFoods = mutableStateListOf<FoodData>()
    var dinnerFoods = mutableStateListOf<FoodData>()
    var snackFoods = mutableStateListOf<FoodData>()
}

internal object Delete {
    val onDeleteBreakfast: (FoodData) -> Unit = { food ->
        ListFood.breakfastFoods.remove(food)
        Globals.TotalProteins -= food.protein.roundToInt()
        Globals.TotalFats -= food.fat.roundToInt()
        Globals.TotalCarbs -= food.carbs.roundToInt()
        Globals.TotalCalories -= food.calories.roundToInt()
    }
    val onDeleteLunch: (FoodData) -> Unit = { food ->
        ListFood.lunchFoods.remove(food)
        Globals.TotalProteins -= food.protein.roundToInt()
        Globals.TotalFats -= food.fat.roundToInt()
        Globals.TotalCarbs -= food.carbs.roundToInt()
        Globals.TotalCalories -= food.calories.roundToInt()
    }
    val onDeleteDinner: (FoodData) -> Unit = { food ->
        ListFood.dinnerFoods.remove(food)
        Globals.TotalProteins -= food.protein.roundToInt()
        Globals.TotalFats -= food.fat.roundToInt()
        Globals.TotalCarbs -= food.carbs.roundToInt()
        Globals.TotalCalories -= food.calories.roundToInt()
    }
    val onDeleteSnack: (FoodData) -> Unit = { food ->
        ListFood.snackFoods.remove(food)
        Globals.TotalProteins -= food.protein.roundToInt()
        Globals.TotalFats -= food.fat.roundToInt()
        Globals.TotalCarbs -= food.carbs.roundToInt()
        Globals.TotalCalories -= food.calories.roundToInt()
    }
}