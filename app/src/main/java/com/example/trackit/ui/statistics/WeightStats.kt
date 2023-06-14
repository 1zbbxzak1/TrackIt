package com.example.trackit.ui.statistics

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.R
import com.example.trackit.data.Weight.DateTimeWrapper
import com.example.trackit.data.Weight.WeightEntry
import com.example.trackit.data.Weight.WeightViewModel
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.Background
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.CaptionColor
import com.example.trackit.ui.theme.WeightEmpty
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.format.DateTimeFormatter

@SuppressLint("UnrememberedMutableState")
@Composable
fun WeightStatistics(
    weightViewModel: WeightViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by weightViewModel.weightUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val dates = runBlocking {
        weightViewModel.getLastTenDates()
    }.reversed()

    val weightData = dataList(weightViewModel, dates)

    Column(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxSize()) {

        Card(
            modifier = Modifier
                .height(320.dp)
                .fillMaxSize(),
            shape = RoundedCornerShape(20.dp),
            elevation = 8.dp,
            backgroundColor = Arsenic
        ) {
            if (uiState.weightList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Используйте приложение,\nчтобы появилась статистика",
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        softWrap = false,
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            color = CaptionColor
                        )
                    )
                }
            }
            else {
                StatsChartForWeight(
                    data = weightData,
                    modifier = Modifier.height(300.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        if (uiState.weightList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Добавьте вес в профиле\nдля построения списка",
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        softWrap = false,
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = WeightEmpty
                        )
                    )
                    Icon(
                        painterResource(id = R.drawable.emptylist),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(182.dp),
                        tint = WeightEmpty
                    )
                }
            }
        } else {
            WeightBodyWithStats(
                weightList = uiState.weightList,
                onDismiss = { weightEntry ->
                    coroutineScope.launch {
                        weightViewModel.deleteWeight(weightEntry)
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun WeightBodyWithStats(
    weightList: List<WeightEntry>,
    onDismiss: (WeightEntry) -> Unit,
    modifier: Modifier = Modifier
){
    WeightListWithStats(weightList, onDismiss, modifier)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun WeightListWithStats(
    weightList: List<WeightEntry>,
    onDismiss: (WeightEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    val sortedWeightList = weightList.sortedWith(
        compareByDescending<WeightEntry> { it.date.year }
            .thenByDescending { it.date.monthValue }
            .thenByDescending { it.date.dayOfMonth }
    )

    LazyColumn(modifier = modifier) {
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp, bottom = 6.dp, start = 5.dp, end = 5.dp)
                            .clip(RoundedCornerShape(5.dp))
                    ) {
                        Background(dismissState = dismissState) {
                            fraction.value = it
                        }
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
            .padding(horizontal = 5.dp)
            .fillMaxWidth()
            .height(40.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
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
                modifier = Modifier.padding(end = 12.dp),
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
    dates: List<DateTimeWrapper>,
): List<Pair<Float, String>> = runBlocking {
    dates.map { dateTimeWrapper ->
        val weight = weightViewModel.getWeightByDate(dateTimeWrapper.date, dateTimeWrapper.time)
        val fDate = dateTimeWrapper.date.format(DateTimeFormatter.ofPattern("dd.MM"))
        weight.toFloat() to fDate
    }
}
