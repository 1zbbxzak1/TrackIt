package com.example.trackit.ui.workout.exercise

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.data.workout.CardioExercise
import com.example.trackit.data.workout.Exercise
import com.example.trackit.data.workout.WorkoutEntity
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.navigation.TopBarWithLabel
import com.example.trackit.ui.theme.AndroidGreen
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.workout.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutExerciseScreen(
    modifier: Modifier = Modifier,
    categoryId: Int?,
    navigateBack: () -> Unit,
    navigateToWorkoutPage: () -> Unit,
    selectedDate: LocalDate = LocalDate.now(),
    searchedText: String = "",
    viewModel: WorkoutExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    viewModel.updateSelectedCategory(categoryId ?: -1)
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val uiState by viewModel.exerciseUiState.collectAsState()
    val creationDialogState = remember { mutableStateOf(false) }
    val addDialogState = remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    var selectedExercise by remember { mutableStateOf<Exercise>(CardioExercise(name = "", time = Duration.ZERO)) }

    Column {
            TopBarWithLabel(
                label = selectedCategory.name,
                icon = selectedCategory.icon,
                navigateBack = navigateBack
            )

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
                searchedText = searchedText,
                modifier = modifier.padding(top = 8.dp)
            )
        }

    if (creationDialogState.value) {
        CreateNewExerciseDialog(
            onAddExercise = { exercise ->
                coroutineScope.launch {
                    viewModel.updateExercise(exercise)
                }
            },
            onDismiss = { creationDialogState.value = false }
        )
    } else if (addDialogState.value) {
        ExerciseDialog(
            selectedExercise = selectedExercise,
            onAddExercise = { exercise ->
                coroutineScope.launch {
                    viewModel.insertWorkoutEntity(
                        WorkoutEntity(
                            0,
                            exercise.name,
                            exercise,
                            selectedCategory,
                            selectedDate,
                            false
                        )
                    )
                }
                navigateToWorkoutPage()
            },
            onDismiss = { addDialogState.value = false }
        )
    }
}

@Composable
private fun WorkoutExerciseBody(
    itemList: List<Exercise>, onClick: (Exercise) -> Unit,
    textState: MutableState<TextFieldValue>,
    onDelete: (Exercise) -> Unit,
    searchedText: String = "",
    modifier: Modifier = Modifier
){
    WorkoutExerciseList(itemList, onClick, textState, onDelete, searchedText, modifier = modifier)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun WorkoutExerciseList(
    itemList: List<Exercise>, onClick: (Exercise) -> Unit,
    textState: MutableState<TextFieldValue>,
    onDelete: (Exercise) -> Unit,
    searchedText: String = "",
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

            if (filteredItems.isEmpty()) filteredItems = itemList

            item {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier.height(25.dp))
                    Text(text = "Упражнения", style = MaterialTheme.typography.h4)
                    Spacer(modifier.height(25.dp))
                }
            }

            items(filteredItems.reversed(), key = { item -> item.id }, itemContent = { item ->
                val dismissThreshold = 0.25f
                val currentFraction = remember { mutableStateOf(0f) }

                var willDismissDirection: DismissDirection? by remember {
                    mutableStateOf(null)
                }
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        when(it){
                            DismissValue.DismissedToStart -> {
                                if (currentFraction.value >= dismissThreshold && currentFraction.value < 1.0f) {
                                    onDelete(item)
                                }
                                currentFraction.value >= dismissThreshold && currentFraction.value < 1.0f
                            }
                            else -> false
                        }
                    }
                )

                willDismissDirection = when(dismissState.targetValue){
                    DismissValue.Default -> null
                    else ->DismissDirection.EndToStart
                }

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = {
                        FractionalThreshold(dismissThreshold)
                    },
                    modifier = Modifier
                        .animateItemPlacement(),
                    background = {
                        SwipeBackground(dismissState = dismissState) { currentFraction.value = it }
                    },
                    dismissContent = {
                        WorkoutExerciseItem(item, onClick, searchedText)
                    }
                )
            })

            item(){
                Spacer(modifier.height(100.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutExerciseItem(
    item: Exercise,
    onClick: (Exercise) -> Unit,
    searchedText: String = "",
    modifier: Modifier = Modifier
) {
    if (item.name.isNotBlank()) {
        Card(
            onClick = { onClick(item) },
            shape = RoundedCornerShape(20.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
            , elevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val highlightedText = getHighlightedText(item.name, searchedText)
                Text(
                    text = highlightedText,
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        color = Arsenic
                    ),
                    modifier = Modifier.weight(10f),
                    overflow = TextOverflow.Ellipsis
                )

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