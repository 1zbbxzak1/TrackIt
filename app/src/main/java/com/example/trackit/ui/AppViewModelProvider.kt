package com.example.trackit.ui

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trackit.TrackItApplication
import com.example.trackit.ui.workout.WorkoutViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.example.trackit.data.Weight.WeightViewModel
import com.example.trackit.ui.workout.category.WorkoutCategoryViewModel
import com.example.trackit.ui.workout.exercise.WorkoutExerciseViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            WorkoutViewModel(trackItApplication().container.workoutItemsRepository)
        }

        initializer {
            WorkoutCategoryViewModel(trackItApplication().container.workoutCategoryRepository)
        }

        initializer {
            WorkoutExerciseViewModel(
                trackItApplication().container.workoutItemsRepository,
                trackItApplication().container.workoutCategoryRepository
            )
        }
        initializer {
            WeightViewModel(trackItApplication().container.weightRepository)
        }
    }
}

fun CreationExtras.trackItApplication(): TrackItApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TrackItApplication)