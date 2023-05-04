package com.example.trackit.ui.workout.exercise

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.data.workout.CardioExercise
import com.example.trackit.data.workout.Exercise
import com.example.trackit.data.workout.WorkoutEntity
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.navigation.ExerciseTopBar
import com.example.trackit.ui.theme.AndroidGreen
import com.example.trackit.ui.theme.Arsenic
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
        topBar = { ExerciseTopBar(selectedCategory.name,selectedCategory.icon, navigateBack) },
    ) {

        Column() {
            Card(
                onClick = { creationDialogState.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Создать упражнение",
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = Arsenic
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        Icons.Rounded.Add, contentDescription = null,
                        tint = Arsenic, modifier = Modifier.size(40.dp)
                    )
                }
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
            ExerciseDialog(
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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
//            val searchedText = textState.value.text
//            filteredItems = if (searchedText.isEmpty()){
//                itemList
//            } else {
//                val resultList = ArrayList<Exercise>()
//                for (item in itemList){
//                    if (item.name.lowercase(Locale.getDefault())
//                            .contains(searchedText.lowercase(Locale.getDefault()))){
//                        resultList.add(item)
//                    }
//                }
//                resultList
//            }

            item {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier.height(25.dp))
                    Text(text = "Упражнения", style = MaterialTheme.typography.h4)
                    Spacer(modifier.height(25.dp))
                }
            }

            items(itemList) { item ->
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
            shape = RoundedCornerShape(20.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                //.height(60.dp),
            ,elevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = item.name, style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Arsenic
                ), modifier = Modifier.weight(10f))

                IconButton(onClick = { onDelete(item) }) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Удалить упражнение",
                        tint = Arsenic)
                }

                Icon(
                    Icons.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AndroidGreen,
                    modifier = Modifier
                        .size(width = 45.dp, height = 45.dp)
                )
            }
        }
    }
}