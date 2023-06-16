package com.example.trackit.data.weight

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

@Dao
interface WeightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weightEntry: WeightEntry)

    @Query("DELETE FROM weight_entries WHERE id = :id")
    suspend fun deleteWeightById(id: Long)

    @Query("SELECT * from weight_entries ORDER BY id DESC")
    fun getAllWeight(): Flow<List<WeightEntry>>

    @Query("SELECT * FROM weight_entries WHERE date = :date")
    fun getWeights(date: LocalDate): Flow<List<WeightEntry>>

    @Query("SELECT DISTINCT date, time FROM weight_entries ORDER BY date DESC, time DESC LIMIT 10")
    fun getLastTenDates(): Flow<List<DateTimeWrapper>>

    @Query("SELECT weight from weight_entries WHERE date = :date AND time = :time")
    fun getWeightByDate(date: LocalDate, time: LocalTime): Flow<Double>

    @Query("SELECT CAST(weight AS TEXT) AS weightString FROM weight_entries ORDER BY date DESC, time DESC LIMIT 1")
    fun getLastWeight(): Flow<String>
}