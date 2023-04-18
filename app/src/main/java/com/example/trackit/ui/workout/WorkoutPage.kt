package com.example.trackit.ui.workout

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.FloatingButton
import com.example.trackit.data.Screen
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.theme.AntiFlashWhite
import com.example.trackit.ui.theme.Arsenic
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WorkoutPage(
    navigateToEntry: () -> Unit,
    selectedDate: LocalDate = LocalDate.now(),
    modifier: Modifier = Modifier,
    viewModel: WorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    viewModel.updateSelectedDate(selectedDate)
    val workoutUiState by viewModel.workoutUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var editDialogState by remember { mutableStateOf(false) }
    var selectedEntity by remember {
        mutableStateOf(
            WorkoutEntity(
                0,
                "",
                CardioExercise("", Duration.ZERO),
                WorkoutCategory(0, "", mutableListOf()),
                LocalDate.now(),
                false
            )
        )
    }

    Scaffold(floatingActionButton = {
        FloatingButton(Screen.Workout.name, onClick = { navigateToEntry() })
    }) {
        Column(modifier = modifier) {
            Text(
                text = "Тренировки",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Divider()

            Text(text = selectedDate.dayOfMonth.toString() + " " + selectedDate.month
                .getDisplayName(TextStyle.FULL, Locale.getDefault())
                .lowercase()
                .replaceFirstChar { it.titlecase() } + " " + selectedDate.year,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Divider()

            WorkoutBody(
                itemList = workoutUiState.itemList,
                onDismiss = {item -> coroutineScope.launch {
                    viewModel.deleteItem(item)
                }},
                onCheckedChange = {coroutineScope.launch {
                    viewModel.updateItem(it)
                }},
                onEdit = {
                    selectedEntity = it
                    editDialogState = true
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(3f))
        }
    }

    if (editDialogState){
        ExerciseDialog(
            selectedEntity = selectedEntity,
            onEntity = {entity ->
                       coroutineScope.launch {
                           viewModel.updateItem(entity)
                       }
            },
            onDismiss = { editDialogState = false })
    }
}

@Composable
private fun WorkoutBody(
    itemList: List<WorkoutEntity>,
    onDismiss: (WorkoutEntity) -> Unit,
    onCheckedChange: (WorkoutEntity) -> Unit,
    onEdit: (WorkoutEntity) -> Unit,
    modifier: Modifier = Modifier
){
    WorkoutList(itemList, onDismiss, onCheckedChange, onEdit, modifier)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun WorkoutList(
    itemList: List<WorkoutEntity>,
    onDismiss: (WorkoutEntity) -> Unit,
    onCheckedChange: (WorkoutEntity) -> Unit,
    onEdit: (WorkoutEntity) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item{
            Spacer(modifier.height(60.dp))
        }

        items(items = itemList, key = { item -> item.id }, itemContent = {item ->
            val dismissThreshold = 0.45f
            val currentFraction = remember { mutableStateOf(0f) }

            val dismissState = rememberDismissState(
                confirmStateChange = {
                    when(it){
                        DismissValue.DismissedToStart -> {
                            if (currentFraction.value >= dismissThreshold && currentFraction.value < 1.0f) {
                                onDismiss(item)
                            }
                            currentFraction.value >= dismissThreshold && currentFraction.value < 1.0f
                        }
                        else -> false
                    }
                }
            )

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = {
                    FractionalThreshold(dismissThreshold)
                },
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .animateItemPlacement(),
                background = {
                    SwipeBackground(dismissState = dismissState) { currentFraction.value = it }
                },
                dismissContent = {
                    WorkoutItem(item = item, onCheckedChange, onEdit)
                }
            )
        })

        item {
            Spacer(modifier.height(100.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WorkoutItem(
    item: WorkoutEntity,
    onCheckedChange: (WorkoutEntity) -> Unit,
    onEdit: (WorkoutEntity) -> Unit,
    modifier: Modifier = Modifier
){
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(modifier = modifier
        .fillMaxWidth()
        .padding(start = 4.dp, end = 4.dp)
        .clickable(remember { MutableInteractionSource() }, null) { expanded = !expanded },
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.animateContentSize( animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
        )) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
    
                Checkbox(
                    checked = item.completed,
                    onCheckedChange = {
                    onCheckedChange(WorkoutEntity(item.id, item.name, item.exercise, item.category, item.date, it))
                    }
                )
    
                Text(text = item.name, style = MaterialTheme.typography.h5, modifier = Modifier.weight(1f))
    
                Card (
                    modifier = Modifier
                        .clickable(remember { MutableInteractionSource() }, null) {onEdit(item)},
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = AntiFlashWhite,
                ){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        when (item.exercise){
                            is StrengthExercise -> {
                                Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                    Text("${item.exercise.weight}")
                                    Text(text = "x", Modifier.padding(horizontal = 1.dp))
                                    Text("${item.exercise.repeatCount}")
                                    Text(text = "x", Modifier.padding(horizontal = 1.dp))
                                    Text("${item.exercise.approachCount}")
                                }
                            }
                            is CardioExercise -> {
                                Text("${item.exercise.time.toMinutes()} мин")
                            }
                        }
                    }
                }

                Icon(
                    if (!expanded) Icons.Rounded.KeyboardArrowDown
                    else Icons.Rounded.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            AnimatedVisibility(visible = expanded){
                Card(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 6.dp, vertical = 8.dp),
                    elevation = 20.dp,
                    backgroundColor = Arsenic,
                    contentColor = Color.White
                ) {
                    Column(
                        Modifier
                            .fillMaxSize(1f)
                            .padding(horizontal = 6.dp, vertical = 12.dp)) {
                        Text(text = "Категория: ${item.category.name}", style = MaterialTheme.typography.body1)

                        Spacer(Modifier.height(4.dp))

                        when (item.exercise){
                            is StrengthExercise -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Text(
                                        text = "${item.exercise.weight} кг",
                                        style = MaterialTheme.typography.body1
                                    )
                                    Text(
                                        text = "${item.exercise.repeatCount} повторений",
                                        style = MaterialTheme.typography.body1
                                    )
                                    Text(
                                        text = "${item.exercise.approachCount} подходов",
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                            }
                            is CardioExercise -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "${item.exercise.time.toMinutes()} минут",
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun SwipeBackground(dismissState: DismissState, updateFraction: (Float) -> Unit) {

    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> MaterialTheme.colors.background
            else -> Color.Red
        }
    )

    val alignment = Alignment.CenterEnd
    val icon = Icons.Default.Delete
    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        updateFraction(dismissState.progress.fraction)

        Icon(
            icon,
            contentDescription = "Localized description",
            modifier = Modifier.scale(scale)
        )
    }
}