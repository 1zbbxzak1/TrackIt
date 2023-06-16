package com.example.trackit.data.food

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class FoodRepository(
    private val breakfastDao: BreakfastDao,
    private val lunchDao: LunchDao,
    private val dinnerDao: DinnerDao,
    private val snackDao: SnackDao
) {

    suspend fun insertBreakfast(item: Breakfast) = breakfastDao.insertFood(item)

    suspend fun deleteBreakfast(item: Breakfast) = breakfastDao.deleteFoodById(item.id)

    fun getBreakfast(date: LocalDate) = breakfastDao.getFood(date)


    suspend fun insertLunch(item: Lunch) = lunchDao.insertFood(item)

    suspend fun deleteLunch(item: Lunch) = lunchDao.deleteFoodById(item.id)

    fun getLunch(date: LocalDate) = lunchDao.getFood(date)


    suspend fun insertDinner(item: Dinner) = dinnerDao.insertFood(item)

    suspend fun deleteDinner(item: Dinner) = dinnerDao.deleteFoodById(item.id)

    fun getDinner(date: LocalDate) = dinnerDao.getFood(date)


    suspend fun insertSnack(item: Snack) = snackDao.insertFood(item)

    suspend fun deleteSnack(item: Snack) = snackDao.deleteFoodById(item.id)

    fun getSnack(date: LocalDate) = snackDao.getFood(date)
}

class TotalRepository(private val totalDao: TotalDao) {

    fun getLastTenDates(): Flow<List<LocalDate>> =
        totalDao.getLastTenDates()

    fun getAverageCalories(): Flow<Int> =
        totalDao.getAverageCalories()

    fun getPopularFood(): Flow<FoodCount> {
        return totalDao.getPop()
    }

    fun getProteins(date: LocalDate): Flow<Int> =
        totalDao.getProteins(date)

    fun getFats(date: LocalDate): Flow<Int> =
        totalDao.getFats(date)

    fun getCarbs(date: LocalDate): Flow<Int> =
        totalDao.getCarbs(date)

    fun getCalories(date: LocalDate): Flow<Int> =
        totalDao.getCalories(date)

    suspend fun upOrInNutrients(item: Total) =
        totalDao.upOrInNutrients(item)

    suspend fun delNutrients(date: LocalDate, delProteins: Int, delFats: Int, delCarbs: Int, delCalories: Int) =
        totalDao.delNutrients(date, delProteins, delFats, delCarbs, delCalories)
}

