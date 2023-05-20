package com.example.trackit.ui.statistics

import StatChart
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.workout.WorkoutViewModel
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun WorkoutStatistics(
    workoutViewModel: WorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val dates = runBlocking {
        workoutViewModel.getLastTenDatesWithCompletedExercise()
    }.reversed()

    val workoutData = generateWorkoutLineDataList(workoutViewModel, dates)

    Card(
        modifier = Modifier.padding(vertical = 50.dp, horizontal = 10.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 6.dp,
        backgroundColor = Arsenic
    ) {
        StatChart(
            data = workoutData,
            modifier = Modifier
                .height(600.dp)
                .padding(5.dp)
        )
    }
}

private fun generateWorkoutLineDataList(
    workoutViewModel: WorkoutViewModel,
    dates: List<LocalDate>
): List<Pair<Float, String>> {
    val lineDataList = mutableListOf<Pair<Float, String>>()

    for (date in dates) {
        val count = runBlocking {
            workoutViewModel.getCompletedItemCountOnDate(date)
        }
        val formattedMonth = DateTimeFormatter.ofPattern("MM").format(date)
        val formattedDate = "${date.dayOfMonth}.${formattedMonth}"
        val lineData = Pair(count.toFloat(), formattedDate)
        lineDataList.add(lineData)
    }

    return lineDataList
}