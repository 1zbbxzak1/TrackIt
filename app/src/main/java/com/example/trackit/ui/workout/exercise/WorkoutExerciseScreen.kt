package com.example.trackit.ui.workout.exercise

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.FloatingButton
import com.example.trackit.data.ExerciseType
import com.example.trackit.data.Screen
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.navigation.WorkoutEditTopBar
import com.example.trackit.ui.theme.TrackItTheme
import com.example.trackit.ui.workout.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
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
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val uiState by viewModel.exerciseUiState.collectAsState()
    val creationDialogState = remember { mutableStateOf(false) }
    val addDialogState = remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    var selectedExercise: Exercise = CardioExercise("", Duration.ZERO)

    Scaffold(
        topBar = { WorkoutEditTopBar(textState, navigateBack = navigateBack) },
    ) {

        Column() {
            Card(
                onClick = { creationDialogState.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, top = 8.dp),
                elevation = 12.dp
            ) {
                Row(
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Создать упражнение",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        Icons.Rounded.AddCircle, contentDescription = "создать упражнение",
                        modifier = Modifier.padding(start = 8.dp, end = 12.dp)
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier.height(25.dp))
                Text(text = "Упражнения", style = MaterialTheme.typography.h4)
                Spacer(modifier.height(25.dp))
                Divider()
            }

            WorkoutExerciseBody(
                itemList = uiState.itemList,
                onClick = {exercise ->
                    selectedExercise = exercise
                    addDialogState.value = true
                },
                textState,
                onDelete = {
                    coroutineScope.launch {
                        viewModel.deleteItem(it)
                    }
                },
                modifier = modifier.padding(top = 8.dp)
            )
        }


        if (creationDialogState.value) {
            CreateNewExerciseDialog(
                onAddExercise = {exercise ->
                    coroutineScope.launch {
                        viewModel.updateExercise(exercise)
                    }
                },
                onDismiss = { creationDialogState.value = false }
            )
        } else if (addDialogState.value){
            AddExerciseDialog(
                selectedExercise = selectedExercise,
                onAddExercise = { exercise ->
                    coroutineScope.launch {
                        viewModel.insertWorkoutEntity(WorkoutEntity(0, exercise.name, exercise, selectedCategory, selectedDate, false))
                    }
                    navigateToWorkoutPage()
                },
                onDismiss = { addDialogState.value = false }
            )
        }
    }
}

@Composable
private fun WorkoutExerciseBody(
    itemList: List<Exercise>, onClick: (Exercise) -> Unit,
    textState: MutableState<TextFieldValue>,
    onDelete: (Exercise) -> Unit,
    modifier: Modifier = Modifier
){
    WorkoutExerciseList(itemList, onClick, textState, onDelete, modifier = modifier)
}

@Composable
private fun WorkoutExerciseList(
    itemList: List<Exercise>, onClick: (Exercise) -> Unit,
    textState: MutableState<TextFieldValue>,
    onDelete: (Exercise) -> Unit,
    modifier: Modifier = Modifier
){
    val state = rememberLazyListState()
    var filteredItems: List<Exercise>

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(
            state = state,
            modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val searchedText = textState.value.text
            filteredItems = if (searchedText.isEmpty()){
                itemList
            } else {
                val resultList = ArrayList<Exercise>()
                for (item in itemList){
                    if (item.name.lowercase(Locale.getDefault())
                            .contains(searchedText.lowercase(Locale.getDefault()))){
                        resultList.add(item)
                    }
                }
                resultList
            }

            items(filteredItems) { item ->
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
            onClick = { onClick(item) },
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