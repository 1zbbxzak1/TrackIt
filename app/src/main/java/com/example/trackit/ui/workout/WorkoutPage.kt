package com.example.trackit.ui.workout

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.FloatingButton
import com.example.trackit.data.Screen
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.theme.TrackItTheme
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

            Spacer(modifier = Modifier.height(8.dp))

            WorkoutBody(itemList = workoutUiState.itemList, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Composable
private fun WorkoutBody(itemList: List<WorkoutEntity>, modifier: Modifier = Modifier){
    WorkoutList(itemList = itemList, modifier = modifier)
}

@Composable
private fun WorkoutList(itemList: List<WorkoutEntity>, modifier: Modifier = Modifier){
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(itemList) {item ->
            WorkoutItem(item = item)
        }
    }
}

@Composable
private fun WorkoutItem(item: WorkoutEntity, modifier: Modifier = Modifier){
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(start = 4.dp, end = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.name, style = MaterialTheme.typography.h5, modifier = Modifier.weight(1f))
            Icon(
                Icons.Rounded.Edit,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp, end = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewWorkoutPage(){
    TrackItTheme {

    }
}