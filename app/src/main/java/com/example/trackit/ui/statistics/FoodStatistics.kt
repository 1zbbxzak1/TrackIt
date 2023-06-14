package com.example.trackit.ui.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.data.food.TotalViewModel
import com.example.trackit.ui.AppViewModelProvider
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FoodStatistics(
    modifier: Modifier = Modifier,
    totalViewModel: TotalViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val dates = runBlocking {
        totalViewModel.getLastTenDates()
    }.reversed()

    val foodData = dataList(totalViewModel, dates)

    val averageCalories = runBlocking {
        totalViewModel.getAverageCalories()
    }

    val popularFood = runBlocking {
        totalViewModel.getMostPopularFood()
    }

    Column(modifier = modifier.fillMaxSize()) {

        StatisticsCard(label = "Среднее количество калорий  ", data = "$averageCalories / день")

        Spacer(Modifier.height(10.dp))

        StatisticsCard(label = "Любимый продукт", data = popularFood.ifEmpty { "Нет данных" })

        Spacer(Modifier.height(40.dp))

        GraphCard(data = foodData){modifier, data ->
            LineGraph(modifier, data)
        }
    }
}

private fun dataList(
    totalViewModel: TotalViewModel,
    dates: List<LocalDate>
): List<Pair<Float, String>> = dates.map { date ->
    val calories = runBlocking { totalViewModel.getCalories(date) }
    val fDate = date.format(DateTimeFormatter.ofPattern("dd.MM"))
    calories.toFloat() to fDate
}