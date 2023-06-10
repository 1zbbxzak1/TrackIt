package com.example.trackit.ui.statistics

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.data.Weight.WeightEntry
import com.example.trackit.data.Weight.WeightViewModel
import com.example.trackit.data.food.AddWeightDialog
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.Background
import com.example.trackit.ui.theme.Arsenic
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun WeightStats(
    selectedDate: LocalDate = LocalDate.now(),
    weightViewModel: WeightViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by weightViewModel.weightUiState.collectAsState()
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()

    Box {
        Column(modifier = Modifier.padding(vertical = 30.dp, horizontal = 10.dp)) {
            WeightBodyWithStats(
                weightList = uiState.weightList,
                state = textState,
                onDismiss = { weightEntry ->
                    coroutineScope.launch {
                        weightViewModel.deleteWeight(weightEntry)
                    }
                },
                modifier = Modifier.padding(top = 8.dp),
                selectedDate = selectedDate
            )
        }
    }
}

@Composable
private fun WeightBodyWithStats(
    selectedDate: LocalDate = LocalDate.now(),
    weightList: List<WeightEntry>,
    state: MutableState<TextFieldValue>,
    onDismiss: (WeightEntry) -> Unit,
    modifier: Modifier = Modifier
){
    WeightListWithStats(weightList, state, onDismiss, modifier, selectedDate)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun WeightListWithStats(
    weightList: List<WeightEntry>,
    state: MutableState<TextFieldValue>,
    onDismiss: (WeightEntry) -> Unit,
    modifier: Modifier = Modifier,
    date: LocalDate = LocalDate.now(),
    time: LocalTime = LocalTime.now(),
    weightViewModel: WeightViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val dialogState = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val dates = runBlocking {
        weightViewModel.getLastTenDatesWithWeight()
    }.reversed()

    val weightData = dataList(weightViewModel, dates)

    val sortedWeightList =
        weightList.sortedByDescending { it.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) }

    LazyColumn(modifier = modifier) {
        item {
            Card(
                modifier = Modifier
                    .height(320.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = 8.dp,
                backgroundColor = Arsenic
            ) {
                StatsChartForW(
                    data = weightData,
                    modifier = Modifier
                        .height(300.dp)
                        .padding(5.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Добавить вес",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
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
                            weightViewModel.insertWeight(
                                WeightEntry(
                                    0,
                                    time,
                                    date,
                                    weight.toDouble()
                                )
                            )
                        }
                        dialogState.value = false
                    },
                    onDismiss = { dialogState.value = false }
                )
            }
        }
        items(items = sortedWeightList, key = { item -> item.id }, itemContent = { item ->
            val threshold = 0.25f
            val fraction = remember { mutableStateOf(0f) }

            var direction: DismissDirection? by remember {
                mutableStateOf(null)
            }
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    when (it) {
                        DismissValue.DismissedToStart -> {
                            if (fraction.value >= threshold && fraction.value < 1.0f) {
                                onDismiss(item)
                            }
                            fraction.value >= threshold && fraction.value < 1.0f
                        }
                        else -> {
                            false
                        }
                    }
                }
            )

            direction = when (dismissState.targetValue) {
                DismissValue.Default -> null
                else -> DismissDirection.EndToStart
            }

            val hapticFeedback = LocalHapticFeedback.current
            LaunchedEffect(key1 = direction, block = {
                if (direction != null) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            })
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = {
                    FractionalThreshold(threshold)
                },
                modifier = Modifier.animateItemPlacement(),
                background = {
                    Background(dismissState = dismissState) {
                        fraction.value = it
                    }
                },
                dismissContent = {
                    Column(modifier = modifier) {
                        Weight(weight = item)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            )
        })
    }
}

@Composable
private fun Weight(
    weight: WeightEntry,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .height(40.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(10.dp))

            Text(text = weight.weight.toString(), modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Arsenic
                )
            )

            Text(
                text = weight.time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                modifier = Modifier,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Arsenic
                )
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = weight.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                modifier = Modifier,
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Arsenic
                )
            )
        }
    }
}

private fun dataList(
    weightViewModel: WeightViewModel,
    dates: List<LocalDate>
): List<Pair<Float, String>> = dates.map { date ->
    val weight = runBlocking { weightViewModel.getWeightByDate(date) }
    val fDate = date.format(DateTimeFormatter.ofPattern("dd.MM"))
    weight.toFloat() to fDate
}