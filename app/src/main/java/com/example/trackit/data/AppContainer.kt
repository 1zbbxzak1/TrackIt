package com.example.trackit.data

import android.content.Context
import com.example.trackit.data.workout.WorkoutCategoryDatabase
import com.example.trackit.data.workout.WorkoutCategoryRepository
import com.example.trackit.data.workout.WorkoutDatabase
import com.example.trackit.data.workout.WorkoutItemsRepository

interface AppContainer {
    val workoutItemsRepository: WorkoutItemsRepository
    val workoutCategoryRepository: WorkoutCategoryRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val workoutItemsRepository: WorkoutItemsRepository by lazy {
        WorkoutItemsRepository(WorkoutDatabase.getDatabase(context).itemDao())
    }

    override val workoutCategoryRepository: WorkoutCategoryRepository by lazy {
        WorkoutCategoryRepository(WorkoutCategoryDatabase.getDatabase(context).itemDao())
    }
}