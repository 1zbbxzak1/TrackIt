package com.example.trackit.ui.workout.exercise

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackit.ui.workout.Exercise
import com.example.trackit.ui.workout.WorkoutCategoryRepository
import com.example.trackit.ui.workout.WorkoutEntity
import com.example.trackit.ui.workout.WorkoutItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WorkoutExerciseViewModel(
    private val workoutRepository: WorkoutItemsRepository,
    private val categoryRepository: WorkoutCategoryRepository
    ) : ViewModel() {

    var selectedCategoryId = mutableStateOf(-1)

    private val _exercisesUiState = MutableStateFlow(WorkoutExerciseUiState())

    val exerciseUiState: StateFlow<WorkoutExerciseUiState> = _exercisesUiState

    fun updateSelectedCategory(categoryId: Int){
        selectedCategoryId.value = categoryId
        viewModelScope.launch {
            _exercisesUiState.value = WorkoutExerciseUiState(categoryRepository.getItemStream(categoryId).first()?.exercises ?: listOf())
        }
    }

    suspend fun insertWorkoutEntity(item: WorkoutEntity){
        workoutRepository.insertItem(item)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class WorkoutExerciseUiState(val itemList: List<Exercise> = listOf())