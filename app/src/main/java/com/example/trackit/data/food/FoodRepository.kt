package com.example.trackit.data.food

import com.example.trackit.ui.Nutrition.FoodData

class FoodRepository(private val foodDao: FoodDao) {

    suspend fun insert(foodData: FoodData) {
        foodDao.insert(foodData)
    }

    suspend fun getAll(): List<FoodData> {
        return foodDao.getAll()
    }

    suspend fun getById(id: Int): FoodData {
        return foodDao.getById(id)
    }

    suspend fun update(foodData: FoodData) {
        foodDao.update(foodData)
    }

    suspend fun delete(food: FoodData) {
        foodDao.delete(food.id)
    }

//    fun getItemsOnDate(date: LocalDate) = foodDao.getItemsOnDate(date)
}
