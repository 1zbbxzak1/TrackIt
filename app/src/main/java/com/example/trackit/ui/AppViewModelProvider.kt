package com.example.trackit.ui

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trackit.TrackItApplication
import com.example.trackit.ui.workout.WorkoutViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            WorkoutViewModel(trackItApplication().container.workoutItemsRepository)
        }
    }
}

fun CreationExtras.trackItApplication(): TrackItApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TrackItApplication)