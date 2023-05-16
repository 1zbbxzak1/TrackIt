package com.example.trackit.ui.statistics

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.workout.WorkoutViewModel
import com.himanshoe.charty.line.model.LineData
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

    StatChart(data = workoutData)
}

private fun generateWorkoutLineDataList(
    workoutViewModel: WorkoutViewModel,
    dates: List<LocalDate>
): List<LineData> {
    val lineDataList = mutableListOf<LineData>()

    for (date in dates) {
        val count = runBlocking {
            workoutViewModel.getCompletedItemCountOnDate(date)
        }
        val formattedMonth = DateTimeFormatter.ofPattern("MM").format(date)
        val formattedDate = "${date.dayOfMonth}.${formattedMonth}"
        val lineData = LineData(formattedDate, count.toFloat())
        lineDataList.add(lineData)
    }

    return lineDataList
}