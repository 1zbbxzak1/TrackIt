package com.example.trackit.ui.workout

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackit.data.workout.WorkoutEntity
import com.example.trackit.data.workout.WorkoutItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

class WorkoutViewModel(private val repository: WorkoutItemsRepository): ViewModel() {

    val selectedDate = mutableStateOf(LocalDate.now())

    private val _workoutUiState = MutableStateFlow(WorkoutUiState(isLoading = true))

    val workoutUiState: StateFlow<WorkoutUiState> = _workoutUiState

    suspend fun updateItem(item: WorkoutEntity){
        repository.updateItem(item)
        _workoutUiState.value = _workoutUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            _workoutUiState.value = repository.getItemsOnDate(selectedDate.value).map { WorkoutUiState(it) }.first().copy(isLoading = false)
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

    suspend fun getCompletedItemCountOnDate(date: LocalDate): Int {
        return repository.getCompletedItemCountOnDate(date).first()
    }
    suspend fun getLastTenDatesWithCompletedExercise(): List<LocalDate> {
        return repository.getLastTenDatesWithCompletedExercise().first()
    }

    suspend fun getLastSeriesOfDaysWithCompletedWorkout(): List<LocalDate> {
        return repository.getLastSeriesOfDaysWithCompletedWorkout().firstOrNull()?.let { dates ->
            val consecutiveDays = mutableListOf<LocalDate>()
            var previousDate: LocalDate? = null

            for (date in dates) {
                if (previousDate == null || previousDate.minusDays(1) == date) {
                    consecutiveDays.add(date)
                } else {
                    break
                }
                previousDate = date
            }

            consecutiveDays
        } ?: emptyList()
    }

    suspend fun getAverageExercisesPerDay(): Float =
        repository.getAverageExercisesPerDay().firstOrNull() ?: 0.0.toFloat()

    suspend fun getMostPopularCategory(): String =
        repository.getMostPopularCategory().firstOrNull()?.category?.split(":")?.firstOrNull() ?: ""

    suspend fun getMostPopularExercise(): String =
        repository.getMostPopularExercise().firstOrNull()?.exercise?.split(":")?.getOrNull(1) ?: ""
}

data class WorkoutUiState(
    val itemList: List<WorkoutEntity> = listOf(),
    val isLoading: Boolean = false
)