package com.example.trackit.ui.workout.exercise

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.FloatingButton
import com.example.trackit.data.Screen
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

    val coroutineScope = rememberCoroutineScope()
    viewModel.updateSelectedCategory(categoryId ?: -1)
    val uiState by viewModel.exerciseUiState.collectAsState()
    val dialogState = remember { mutableStateOf(false) }


    Scaffold(
        topBar = { WorkoutEditTopBar(title = "Выберите упражнение: <${viewModel.selectedId.value}>", navigateBack = navigateBack) },
        floatingActionButton = {
            FloatingButton(currentRoute = Screen.WorkoutExercise.name, onClick = { dialogState.value = true })
        }
    ) {
        WorkoutExerciseBody(
            itemList = uiState.itemList,
            onClick = {exercise ->
                coroutineScope.launch {
                    viewModel.insertWorkoutEntity(WorkoutEntity(0, exercise.name, exercise, selectedDate, false))
                }
                navigateToWorkoutPage()
            },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteItem(it)
                }
            },
            modifier = modifier.padding(top = 8.dp)
        )

        if (dialogState.value) {
            AddExerciseDialog(
                onAddExercise = {exercise ->
                    coroutineScope.launch {
                        viewModel.updateExercise(exercise)
                    }
                },
                onDismiss = { dialogState.value = false }
            )
        }
    }
}

@Composable
private fun WorkoutExerciseBody(
    itemList: List<Exercise>, onClick: (Exercise) -> Unit,
    onDelete: (Exercise) -> Unit,
    modifier: Modifier = Modifier
){
    WorkoutExerciseList(itemList, onClick, onDelete, modifier = modifier)
}

@Composable
private fun WorkoutExerciseList(
    itemList: List<Exercise>, onClick: (Exercise) -> Unit,
    onDelete: (Exercise) -> Unit,
    modifier: Modifier = Modifier
){
    val state = rememberLazyListState()

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(
            state = state,
            modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(){
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier.height(50.dp))
                    Text(text = "Упражнения", style = MaterialTheme.typography.h4)
                    Spacer(modifier.height(50.dp))
                    Divider()
                }
            }

            items(items = itemList, key = { item -> item.name }) { item ->
                WorkoutExerciseItem(item, onClick, onDelete)
            }

            item(){
                Spacer(modifier.height(100.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WorkoutExerciseItem(
    item: Exercise, onClick: (Exercise) -> Unit,
    onDelete: (Exercise) -> Unit,
    modifier: Modifier = Modifier
){
    if (item.name.isNotBlank()){
        Card(
            onClick = {onClick(item)},
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = item.name, style = MaterialTheme.typography.h5, modifier = Modifier.weight(10f))

                IconButton(onClick = { onDelete(item) }) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Удалить упражнение")
                }

                Icon(
                    Icons.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp, end = 12.dp)
                )
            }
        }
    }
}

@Composable
fun AddExerciseDialog(
    onAddExercise: (Exercise) -> Unit,
    onDismiss: () -> Unit
) {
    var categoryName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Добавление упражнения", style = MaterialTheme.typography.h5)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        modifier = Modifier.padding(32.dp),
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        label = { Text("Название упражнения:") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(){
                        Row {
                            Button(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(30.dp)
                            ) {
                                Text(text = "Отмена", style = MaterialTheme.typography.h6)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Button(onClick = {
                                onAddExercise(Exercise(categoryName))
                                onDismiss()
                            },
                                shape = RoundedCornerShape(30.dp),
                                enabled = categoryName.isNotBlank()
                            ) {
                                Text(text = "Добавить", style = MaterialTheme.typography.h6)
                            }
                        }
                    }
                }
            }
        }
    }
}