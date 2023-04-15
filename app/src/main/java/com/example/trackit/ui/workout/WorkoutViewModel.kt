package com.example.trackit.ui.workout

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

class WorkoutViewModel(private val repository: WorkoutItemsRepository): ViewModel() {

    val selectedDate = mutableStateOf(LocalDate.now())


    private val _workoutUiState = MutableStateFlow(WorkoutUiState())

    val workoutUiState: StateFlow<WorkoutUiState> = _workoutUiState

    suspend fun updateItem(item: WorkoutEntity){
        repository.updateItem(item)
        viewModelScope.launch {
            _workoutUiState.value = repository.getItemsOnDate(selectedDate.value).map { WorkoutUiState(it) }.first()
        }
    }

    fun updateSelectedDate(date: LocalDate){
        selectedDate.value = date
        viewModelScope.launch {
            _workoutUiState.value = repository.getItemsOnDate(date).map { WorkoutUiState(it) }.first()
        }
    }

    suspend fun deleteItem(item: WorkoutEntity){
        repository.deleteItem(item)
        viewModelScope.launch {
            _workoutUiState.value = repository.getItemsOnDate(selectedDate.value).map { WorkoutUiState(it) }.first()
        }
    }
}

data class WorkoutUiState(val itemList: List<WorkoutEntity> = listOf())