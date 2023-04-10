package com.example.trackit.ui.workout.exercise

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackit.ui.workout.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorkoutExerciseViewModel(
    private val workoutRepository: WorkoutItemsRepository,
    private val categoryRepository: WorkoutCategoryRepository
    ) : ViewModel() {

    var selectedCategoryId = mutableStateOf(-1)

    private val _exercisesUiState = MutableStateFlow(WorkoutExerciseUiState())

    val exerciseUiState: StateFlow<WorkoutExerciseUiState> = _exercisesUiState

    /*
    private var selectedCategory = categoryRepository.getItemStream(selectedCategoryId.value)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = WorkoutCategory(0, "", listOf())
        )


     */

    //var exercises = MutableStateFlow(WorkoutExerciseUiState()).asStateFlow()

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