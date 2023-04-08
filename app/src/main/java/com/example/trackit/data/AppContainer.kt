package com.example.trackit.data

import android.content.Context
import com.example.trackit.ui.workout.WorkoutDatabase
import com.example.trackit.ui.workout.WorkoutItemsRepository

interface AppContainer {
    val workoutItemsRepository: WorkoutItemsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val workoutItemsRepository: WorkoutItemsRepository by lazy {
        WorkoutItemsRepository(WorkoutDatabase.getDatabase(context).itemDao())
    }
}