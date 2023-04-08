package com.example.trackit.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class WorkoutViewModel(private val repository: WorkoutItemsRepository): ViewModel() {

    val workoutUiState: StateFlow<WorkoutUiState> =
        repository.getAllItemsStream().map { WorkoutUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WorkoutUiState()
            )

    suspend fun insertItem(){
        repository.insertItem(WorkoutEntity(0, "Name", Exercise("Exercise"), LocalDate.now()))
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class WorkoutUiState(val itemList: List<WorkoutEntity> = listOf())