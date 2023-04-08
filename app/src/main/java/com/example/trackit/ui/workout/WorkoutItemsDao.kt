package com.example.trackit.ui.workout

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutItemsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: WorkoutEntity)

    @Update
    suspend fun update(item: WorkoutEntity)

    @Delete
    suspend fun delete(item: WorkoutEntity)

    @Query("SELECT * from workout_items WHERE id = :id")
    fun getItem(id: Int): Flow<WorkoutEntity>

    @Query("SELECT * from workout_items ORDER BY name ASC")
    fun getAllItems(): Flow<List<WorkoutEntity>>
}