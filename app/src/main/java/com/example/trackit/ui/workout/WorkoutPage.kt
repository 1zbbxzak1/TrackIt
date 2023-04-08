package com.example.trackit.ui.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.data.Screen
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.theme.TrackItTheme
import kotlinx.coroutines.launch

@Composable
fun WorkoutPage(
    modifier: Modifier = Modifier,
    viewModel: WorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val workoutUiState by viewModel.workoutUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        Text(text = Screen.Workout.name, fontSize = 50.sp)

        Button(onClick = { coroutineScope.launch {  viewModel.insertItem()} }) {
            Text(text = "ADD")
        }

        WorkoutBody(itemList = workoutUiState.itemList)
    }
}

@Composable
private fun WorkoutBody(itemList: List<WorkoutEntity>){
    WorkoutList(itemList = itemList)
}

@Composable
private fun WorkoutList(itemList: List<WorkoutEntity>){
    LazyColumn{
        items(itemList) {item ->
            WorkoutItem(item = item)
        }
    }
}

@Composable
private fun WorkoutItem(item: WorkoutEntity){
    Text(text = item.name)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewWorkoutPage(){
    TrackItTheme {

    }
}