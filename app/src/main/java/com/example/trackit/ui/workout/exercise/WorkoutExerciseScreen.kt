package com.example.trackit.ui.workout.exercise

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.navigation.WorkoutEditTopBar
import com.example.trackit.ui.workout.Exercise
import com.example.trackit.ui.workout.WorkoutEntity
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WorkoutExerciseScreen(
    categoryId: Int?,
    navigateBack: () -> Unit,
    navigateToWorkoutPage: () -> Unit,
    selectedDate: LocalDate = LocalDate.now(),
    modifier: Modifier = Modifier,
    viewModel: WorkoutExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    viewModel.updateSelectedCategory(categoryId ?: -1)
    val uiState by viewModel.exerciseUiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = { WorkoutEditTopBar(title = "Выберите упражнение: <${viewModel.selectedCategoryId.value}>", navigateBack = navigateBack) },
        modifier = modifier
    ) {
        WorkoutExerciseBody(
            itemList = uiState.itemList,
            onClick = {exercise ->
                coroutineScope.launch {
                    viewModel.insertWorkoutEntity(WorkoutEntity(0, exercise.name, exercise, selectedDate))
                }
                navigateToWorkoutPage()
            },
        )
    }
}

@Composable
private fun WorkoutExerciseBody(
    itemList: List<Exercise>, onClick: (Exercise) -> Unit,
    modifier: Modifier = Modifier
){
    WorkoutExerciseList(itemList, onClick, modifier = modifier)
}

@Composable
private fun WorkoutExerciseList(
    itemList: List<Exercise>, onClick: (Exercise) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)){
        items(itemList){item ->
            WorkoutExerciseItem(item, onClick)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WorkoutExerciseItem(
    item: Exercise, onClick: (Exercise) -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        onClick = {onClick(item)},
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
    ) {
        Row(modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)) {
            Text(text = item.name, style = MaterialTheme.typography.h4)
        }
    }
}