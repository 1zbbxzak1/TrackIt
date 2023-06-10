package com.example.trackit.data.Weight

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WeightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weightEntry: WeightEntry)

    @Query("DELETE FROM weight_entries WHERE id = :id")
    suspend fun deleteWeightById(id: Long)

    @Query("SELECT * from weight_entries WHERE id = :id")
    fun getWeight(id: Int): Flow<WeightEntry>

    @Query("SELECT * from weight_entries ORDER BY id DESC")
    fun getAllWeight(): Flow<List<WeightEntry>>

    @Query("SELECT * FROM weight_entries WHERE date = :date")
    fun getWeights(date: LocalDate): Flow<List<WeightEntry>>

    @Query("SELECT DISTINCT date FROM weight_entries ORDER BY date DESC LIMIT 10")
    fun getLastTenDates(): Flow<List<LocalDate>>

    @Query("SELECT weight from weight_entries WHERE date = :date")
    fun getWeightByDate(date: LocalDate): Flow<Double>

    @Query("SELECT weight FROM weight_entries ORDER BY date DESC LIMIT 1")
    fun getLastWeight(): Flow<Double>
}