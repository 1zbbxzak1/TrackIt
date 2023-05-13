package com.example.trackit.data.food

import androidx.room.*
import com.example.trackit.data.workout.WorkoutEntity
import com.example.trackit.ui.Nutrition.FoodData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodData: FoodData)

    @Query("SELECT * FROM food_items")
    suspend fun getAll(): List<FoodData>

    @Query("SELECT * FROM food_items WHERE id = :id")
    suspend fun getById(id: Int): FoodData

    @Update
    suspend fun update(food: FoodData)

    @Query("DELETE FROM food_items WHERE id = :id")
    suspend fun delete(id: Int)

//    @Query("SELECT * from food_items WHERE date = :date")
//    fun getItemsOnDate(date: LocalDate): Flow<List<FoodData>>
}