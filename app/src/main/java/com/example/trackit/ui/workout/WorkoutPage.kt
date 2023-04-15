package com.example.trackit.ui.workout

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.FloatingButton
import com.example.trackit.data.Screen
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.theme.TrackItTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WorkoutPage(
    navigateToEntry: () -> Unit, //TODO: navigateToUpdate: (id: Int) -> Unit
    selectedDate: LocalDate = LocalDate.now(),
    modifier: Modifier = Modifier,
    viewModel: WorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    viewModel.updateSelectedDate(selectedDate)
    val workoutUiState by viewModel.workoutUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(floatingActionButton = {
        FloatingButton(Screen.Workout.name, onClick = { navigateToEntry() })
    }) {
        Column(modifier = modifier) {
            Text(
                text = "Тренировки",
                fontSize = 50.sp,
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

            Spacer(modifier = Modifier.weight(1f))

            WorkoutBody(
                itemList = workoutUiState.itemList,
                onDismiss = {item -> coroutineScope.launch {
                    viewModel.deleteItem(item)
                }},
                onCheckedChange = {coroutineScope.launch {
                    viewModel.updateItem(it)
                }},
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .weight(3f))
        }
    }
}

@Composable
private fun WorkoutBody(
    itemList: List<WorkoutEntity>,
    onDismiss: (WorkoutEntity) -> Unit,
    onCheckedChange: (WorkoutEntity) -> Unit,
    modifier: Modifier = Modifier
){
    WorkoutList(itemList = itemList, onDismiss = onDismiss, onCheckedChange, modifier = modifier)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun WorkoutList(
    itemList: List<WorkoutEntity>,
    onDismiss: (WorkoutEntity) -> Unit,
    onCheckedChange: (WorkoutEntity) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    WorkoutItem(item = item, onCheckedChange)
                }
            )
        })
    }
}

@Composable
private fun WorkoutItem(
    item: WorkoutEntity,
    onCheckedChange: (WorkoutEntity) -> Unit,
    modifier: Modifier = Modifier
){
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(start = 4.dp, end = 4.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = item.completed,
                onCheckedChange = {
                onCheckedChange(WorkoutEntity(item.id, item.name, item.exercise, item.date, it))
                }
            )

            Text(text = item.name, style = MaterialTheme.typography.h5, modifier = Modifier.weight(1f))

            Icon(
                Icons.Rounded.Edit,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp, end = 12.dp)
            )
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewWorkoutPage(){
    TrackItTheme {

    }
}