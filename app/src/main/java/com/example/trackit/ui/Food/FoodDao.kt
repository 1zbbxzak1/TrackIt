package com.example.trackit.ui.Food

import androidx.room.*
import com.example.trackit.ui.Nutrition.FoodData

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_table")
    fun getAll(): List<FoodData>

    @Insert
    fun insert(food: FoodData)

    @Update
    fun update(food: FoodData)

    @Delete
    fun delete(food: FoodData)
}

