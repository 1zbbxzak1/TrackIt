package com.example.trackit.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.data.Weight.WeightEntry
import com.example.trackit.data.Weight.WeightViewModel
import com.example.trackit.data.food.AddWeightDialog
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.theme.Arsenic
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WeightStats(
    weightViewModel: WeightViewModel = viewModel(factory = AppViewModelProvider.Factory),
    date: LocalDate = LocalDate.now()
){
    val dialogState = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val dates = runBlocking {
        weightViewModel.getLastNineDatesWithWeight()
    }.reversed()

    val weightData = dataList(weightViewModel, dates)

    LazyColumn {
        item {
            Box(Modifier.padding(vertical = 30.dp, horizontal = 10.dp)) {
                Card(
                    modifier = Modifier
                        .height(320.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = 6.dp,
                    backgroundColor = Arsenic
                ) {
                    StatsChartForW(
                        data = weightData,
                        modifier = Modifier
                            .height(300.dp)
                            .padding(5.dp)
                    )
                }
            }
        }
        item {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    onClick = { dialogState.value = true },
                    modifier = Modifier
                        .width(173.dp)
                        .height(45.dp),
                    shape = RoundedCornerShape(30.dp),
                    elevation = 8.dp,
                    backgroundColor = Arsenic
                ) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Добавить вес",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                color = White
                            )
                        )
                    }
                }
            }

            if (dialogState.value) {
                AddWeightDialog(
                    onAddWeight = { weight ->
                        coroutineScope.launch {
                            weightViewModel.insertWeight(WeightEntry(0, date, weight.toDouble()))
                        }
                        dialogState.value = false
                    },
                    onDismiss = { dialogState.value = false }
                )
            }
        }

        item {
            Box(Modifier.padding(vertical = 30.dp, horizontal = 10.dp)) {
                Card(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp),
                    elevation = 6.dp
                ) {

                }
            }
        }
    }
}

private fun dataList(
    weightViewModel: WeightViewModel,
    dates: List<LocalDate>
): List<Pair<Float, String>> {
    val lineDataList = mutableListOf<Pair<Float, String>>()

    for (date in dates) {
        val count = runBlocking {
            weightViewModel.getWeightByDate(date)
        }
        val formattedMonth = DateTimeFormatter.ofPattern("MM").format(date)
        val formattedDate = "${date.dayOfMonth}.${formattedMonth}"
        val lineData = Pair(count.toFloat(), formattedDate)
        lineDataList.add(lineData)
    }

    return lineDataList
}