package com.example.trackit.ui.workout

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class WorkoutViewModel(private val repository: WorkoutItemsRepository): ViewModel() {

    val selectedDate = mutableStateOf(LocalDate.now())


    private val _workoutUiState = MutableStateFlow(WorkoutUiState())

    val workoutUiState: StateFlow<WorkoutUiState> = _workoutUiState

    fun updateSelectedDate(date: LocalDate){
        selectedDate.value = date
        viewModelScope.launch {
            _workoutUiState.value = repository.getItemsOnDate(date).map { WorkoutUiState(it) }.first()
            Log.d("CURRENT_WORKOUT_LIST", _workoutUiState.value.itemList.toString())
        }
    }

    suspend fun insertItem(date: LocalDate){
        repository.insertItem(WorkoutEntity(0, "Name", Exercise("Exercise"), date))
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class WorkoutUiState(val itemList: List<WorkoutEntity> = listOf())