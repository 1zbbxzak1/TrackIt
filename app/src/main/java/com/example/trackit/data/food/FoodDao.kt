package com.example.trackit.data.food

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface BreakfastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Breakfast)

    @Query("DELETE FROM breakfast WHERE id = :id")
    suspend fun deleteFoodById(id: Long)

    @Query("SELECT * FROM breakfast WHERE date = :date")
    fun getFood(date: LocalDate): Flow<List<Breakfast>>
}

@Dao
interface LunchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Lunch)

    @Query("DELETE FROM lunch WHERE id = :id")
    suspend fun deleteFoodById(id: Long)

    @Query("SELECT * FROM lunch WHERE date = :date")
    fun getFood(date: LocalDate): Flow<List<Lunch>>
}

@Dao
interface DinnerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Dinner)

    @Query("DELETE FROM dinner WHERE id = :id")
    suspend fun deleteFoodById(id: Long)

    @Query("SELECT * FROM dinner WHERE date = :date")
    fun getFood(date: LocalDate): Flow<List<Dinner>>
}

@Dao
interface SnackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Snack)

    @Query("DELETE FROM snack WHERE id = :id")
    suspend fun deleteFoodById(id: Long)

    @Query("SELECT * FROM snack WHERE date = :date")
    fun getFood(date: LocalDate): Flow<List<Snack>>
}

@Dao
interface TotalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTotal(total: Total)

    @Query("SELECT * FROM total WHERE date = :date")
    fun getTotal(date: LocalDate): Total?

    @Query("SELECT DISTINCT date FROM total WHERE calories > 0 ORDER BY date DESC LIMIT 10")
    fun getLastTenDates(): Flow<List<LocalDate>>

    @Query("SELECT AVG(calories) FROM total")
    fun getAverageCalories(): Flow<Int>

    @Query("SELECT proteins FROM total WHERE date = :date")
    fun getProteins(date: LocalDate): Flow<Int>

    @Query("SELECT fats FROM total WHERE date = :date")
    fun getFats(date: LocalDate): Flow<Int>

    @Query("SELECT carbs FROM total WHERE date = :date")
    fun getCarbs(date: LocalDate): Flow<Int>

    @Query("SELECT calories FROM total WHERE date = :date")
    fun getCalories(date: LocalDate): Flow<Int>

    @Query(
        "UPDATE total " +
                "SET proteins = proteins + :addProteins, " +
                "fats = fats + :addFats, " +
                "carbs = carbs + :addCarbs, " +
                "calories = calories + :addCalories " +
                "WHERE date = :date"
    )
    suspend fun updateNutrients(
        date: LocalDate,
        addProteins: Int,
        addFats: Int,
        addCarbs: Int,
        addCalories: Int
    )

    @Query(
        "UPDATE total " +
                "SET proteins = proteins - :delProteins, " +
                "fats = fats - :delFats, " +
                "carbs = carbs - :delCarbs, " +
                "calories = calories - :delCalories " +
                "WHERE date = :date"
    )
    suspend fun delNutrients(date: LocalDate, delProteins: Int, delFats: Int, delCarbs: Int, delCalories: Int)

    @Transaction
    suspend fun upOrInNutrients(item: Total) {
        getTotal(item.date)?.let {
            updateNutrients(item.date, item.proteins, item.fats, item.carbs, item.calories)
        } ?: insertTotal(item)
    }

    @Query(
        "SELECT food, COUNT(food) AS count " +
                "FROM (" +
                "SELECT foodLunch AS food FROM lunch " +
                "UNION ALL " +
                "SELECT foodBreakfast AS food FROM breakfast " +
                "UNION ALL " +
                "SELECT foodDinner AS food FROM dinner " +
                "UNION ALL " +
                "SELECT foodSnack AS food FROM snack " +
                ") " +
                "GROUP BY food " +
                "ORDER BY count DESC " +
                "LIMIT 1"
    )
    fun getPop(): Flow<FoodCount>
}