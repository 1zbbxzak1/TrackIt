package com.example.trackit.ui.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FoodStatistics(
    modifier: Modifier = Modifier,
){
    //TODO: Список данных для графика, где Float - значение по оси Ox, String - Дата по оси Oy
    val foodData = mutableListOf<Pair<Float, String>>()

    val caloriesCount by remember { mutableStateOf("153244") }

    val averageCalories by remember { mutableStateOf(3456) }

    val popularFood by remember { mutableStateOf("Гречневая каша") }

    Column(modifier = modifier.fillMaxSize()) {
        StatisticsCard(label = "Общее количество калорий ", data = caloriesCount)

        Spacer(Modifier.height(10.dp))

        StatisticsCard(label = "Среднее количество калорий  ", data = "$averageCalories / день")

        Spacer(Modifier.height(10.dp))

        StatisticsCard(label = "Любимый продукт", data = popularFood)

        Spacer(Modifier.height(40.dp))

        GraphCard(data = foodData){modifier, data ->
            LineGraph(modifier, data)
        }
    }
}