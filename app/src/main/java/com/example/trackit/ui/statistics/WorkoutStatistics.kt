package com.example.trackit.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import java.util.Locale

@Composable
fun WorkoutStatistics(
    modifier: Modifier = Modifier,
    workoutViewModel: WorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val dates = runBlocking {
        workoutViewModel.getLastTenDatesWithCompletedExercise()
    }.reversed()

    val daySeries = runBlocking {
        workoutViewModel.getLastSeriesOfDaysWithCompletedWorkout().count()
    }

    val averageExercises = runBlocking {
        workoutViewModel.getAverageExercisesPerDay()
    }

    val popularCategory = runBlocking {
        workoutViewModel.getMostPopularCategory()
    }

    val popularExercise = runBlocking {
        workoutViewModel.getMostPopularExercise()
    }

    val workoutData = generateWorkoutLineDataList(workoutViewModel, dates)

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        StatisticsCard(label = "Серия упражнений", data = "$daySeries")

        StatisticsCard(label = "Среднее количество упражнений ",
            data = "${String.format(Locale.ENGLISH, "%.1f", averageExercises)} / день")

        StatisticsCard(label = "Любимая категория", data = popularCategory.ifEmpty { "Нет данных" })

        StatisticsCard(label = "Любимое упражнение", data = popularExercise.ifEmpty { "Нет данных" })

        Spacer(Modifier.height(20.dp))

        GraphCard(data = workoutData){modifier, data ->
            LineGraph(modifier, data)
        }
    }
}

private fun generateWorkoutLineDataList(
    workoutViewModel: WorkoutViewModel,
    dates: List<LocalDate>
): List<Pair<Float, String>> {
    val lineDataList = mutableListOf<Pair<Float, String>>()

    dates.forEach {date ->
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