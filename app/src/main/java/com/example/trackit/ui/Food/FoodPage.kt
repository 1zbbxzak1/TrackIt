package com.example.trackit.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.R
import com.example.trackit.data.food.*
import com.example.trackit.ui.Nutrition.*
import java.time.LocalDate
import com.example.trackit.ui.theme.PermanentGeraniumLake
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

@Composable
fun FoodPage(
    navigateToFoodScreen: (String) -> Unit,
    selectedDate: LocalDate = LocalDate.now(),
    breakfastV: BreakfastViewModel = viewModel(factory = AppViewModelProvider.Factory),
    lunchV: LunchViewModel = viewModel(factory = AppViewModelProvider.Factory),
    dinnerV: DinnerViewModel = viewModel(factory = AppViewModelProvider.Factory),
    snackV: SnackViewModel = viewModel(factory = AppViewModelProvider.Factory),
    totalV: TotalViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    breakfastV.updateSelectedDate(selectedDate)
    lunchV.updateSelectedDate(selectedDate)
    dinnerV.updateSelectedDate(selectedDate)
    snackV.updateSelectedDate(selectedDate)

    var breakfastExpanded by remember { mutableStateOf(false) }
    var lunchExpanded by remember { mutableStateOf(false) }
    var dinnerExpanded by remember { mutableStateOf(false) }
    var snackExpanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val breakfastUiState by breakfastV.mealUiState.collectAsState()
    val lunchUiState by lunchV.mealUiState.collectAsState()
    val dinnerUiState by dinnerV.mealUiState.collectAsState()
    val snackUiState by snackV.mealUiState.collectAsState()

    var totalProteins by remember { mutableStateOf(0) }
    var totalFats by remember { mutableStateOf(0) }
    var totalCarbs by remember { mutableStateOf(0) }
    var totalCalories by remember { mutableStateOf(0) }

    LaunchedEffect(selectedDate) {
        breakfastExpanded = false
        lunchExpanded = false
        dinnerExpanded = false
        snackExpanded = false

        totalProteins = totalV.getProteins(selectedDate)
        totalFats = totalV.getFats(selectedDate)
        totalCarbs = totalV.getCarbs(selectedDate)
        totalCalories = totalV.getCalories(selectedDate)
    }

    Column {
        Surface(
            color = MaterialTheme.colors.primaryVariant,
            shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
            modifier = Modifier
                .height(70.dp)
                .zIndex(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painterResource(id = R.drawable.food_label),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }

        Box(
            modifier = Modifier
                .height(94.dp)
                .offset(0.dp, (-7).dp)
        ) {
            Surface(
                color = Color(android.graphics.Color.parseColor("#99CD4E")),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Белки",
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier.padding(start = 10.dp, top = 18.dp)
                        )
                        Text(
                            text = "${totalProteins}",
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .offset(x = 4.dp)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Жиры",
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 18.dp)
                                .offset(x = (-7).dp)
                        )
                        Text(
                            text = "${totalFats}",
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .offset(x = (-6).dp)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Углеводы",
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 18.dp)
                        )
                        Text(
                            text = "${totalCarbs}",
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .offset(x = (-2).dp)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Ккал",
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 18.dp, end = 10.dp)
                        )
                        Text(
                            text = "${totalCalories}",
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .offset(x = (-5).dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
        ) {
            item {
                Spacer(modifier = Modifier.padding(20.dp))
                MealPanel(
                    mealType = "Завтрак",
                    mealIcon = R.drawable.breakfast_icon,
                    foods = breakfastUiState.breakfastList,
                    isExpanded = breakfastExpanded,
                    onPanelClicked = { breakfastExpanded = !breakfastExpanded },
                    onAddButtonClick = { navigateToFoodScreen("Завтрак") },
                    onDismiss = { item ->
                        coroutineScope.launch {
                            breakfastV.deleteFood(item as Breakfast)
                            totalV.delNutrients(
                                selectedDate,
                                item.food.protein.roundToInt(),
                                item.food.fat.roundToInt(),
                                item.food.carbs.roundToInt(),
                                item.food.calories.roundToInt()
                            )

                            totalProteins = runBlocking { totalV.getProteins(selectedDate) }
                            totalFats = runBlocking { totalV.getFats(selectedDate) }
                            totalCarbs = runBlocking { totalV.getCarbs(selectedDate) }
                            totalCalories = runBlocking { totalV.getCalories(selectedDate) }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(if (breakfastExpanded) 16.dp else 0.dp))
            }

            item {
                MealPanel(
                    mealType = "Обед",
                    mealIcon = R.drawable.lunch_icon,
                    foods = lunchUiState.lunchList,
                    isExpanded = lunchExpanded,
                    onPanelClicked = { lunchExpanded = !lunchExpanded },
                    onAddButtonClick = { navigateToFoodScreen("Обед") },
                    onDismiss = { item ->
                        coroutineScope.launch {
                            lunchV.deleteFood(item as Lunch)
                            totalV.delNutrients(
                                selectedDate,
                                item.food.protein.roundToInt(),
                                item.food.fat.roundToInt(),
                                item.food.carbs.roundToInt(),
                                item.food.calories.roundToInt()
                            )

                            totalProteins = runBlocking { totalV.getProteins(selectedDate) }
                            totalFats = runBlocking { totalV.getFats(selectedDate) }
                            totalCarbs = runBlocking { totalV.getCarbs(selectedDate) }
                            totalCalories = runBlocking { totalV.getCalories(selectedDate) }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(if (lunchExpanded) 16.dp else 0.dp))
            }

            item {
                MealPanel(
                    mealType = "Ужин",
                    mealIcon = R.drawable.dinner_icon,
                    foods = dinnerUiState.dinnerList,
                    isExpanded = dinnerExpanded,
                    onPanelClicked = { dinnerExpanded = !dinnerExpanded },
                    onAddButtonClick = { navigateToFoodScreen("Ужин") },
                    onDismiss = { item ->
                        coroutineScope.launch {
                            dinnerV.deleteFood(item as Dinner)
                            totalV.delNutrients(
                                selectedDate,
                                item.food.protein.roundToInt(),
                                item.food.fat.roundToInt(),
                                item.food.carbs.roundToInt(),
                                item.food.calories.roundToInt()
                            )

                            totalProteins = runBlocking { totalV.getProteins(selectedDate) }
                            totalFats = runBlocking { totalV.getFats(selectedDate) }
                            totalCarbs = runBlocking { totalV.getCarbs(selectedDate) }
                            totalCalories = runBlocking { totalV.getCalories(selectedDate) }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(if (dinnerExpanded) 16.dp else 0.dp))
            }

            item {
                MealPanel(
                    mealType = "Перекус",
                    mealIcon = R.drawable.snack_icon,
                    foods = snackUiState.snackList,
                    isExpanded = snackExpanded,
                    onPanelClicked = { snackExpanded = !snackExpanded },
                    onAddButtonClick = { navigateToFoodScreen("Перекус") },
                    onDismiss = { item ->
                        coroutineScope.launch {
                            snackV.deleteFood(item as Snack)
                            totalV.delNutrients(
                                selectedDate,
                                item.food.protein.roundToInt(),
                                item.food.fat.roundToInt(),
                                item.food.carbs.roundToInt(),
                                item.food.calories.roundToInt()
                            )

                            totalProteins = runBlocking { totalV.getProteins(selectedDate) }
                            totalFats = runBlocking { totalV.getFats(selectedDate) }
                            totalCarbs = runBlocking { totalV.getCarbs(selectedDate) }
                            totalCalories = runBlocking { totalV.getCalories(selectedDate) }
                        }
                    }
                )
                Spacer(modifier = Modifier.padding(bottom = 40.dp))
            }
        }
    }
}

@Composable
fun MealPanel(
    mealType: String,
    mealIcon: Int,
    foods: List<Meal>,
    isExpanded: Boolean,
    onPanelClicked: () -> Unit,
    onAddButtonClick: () -> Unit,
    onDismiss: (Meal) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.clickable(onClick = onPanelClicked),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(2.dp)
            ) {
                Image(
                    painter = painterResource(id = mealIcon),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = mealType,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onAddButtonClick,
                    modifier = Modifier.size(50.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    if (foods.isEmpty()) {
                        Text(
                            text = "Добавьте продукт",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 20.dp)
                        )
                    } else {
                        FoodCardList(foods = foods, onDelete = onDismiss)
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize().offset(y = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_down),
                contentDescription = null,
                tint = Color(android.graphics.Color.parseColor("#3B4446")),
                modifier = Modifier.size(23.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun FoodCardList(
    foods: List<Meal>,
    onDelete: (Meal) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White
    ) {

        LazyColumn(modifier = modifier) {
            items(items = foods, key = { item -> item.id }, itemContent = { item ->
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
                                    onDelete(item)
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
                        FoodCard(
                            food = item,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp
                            ),
                            index = item.id
                        )
                    }
                )

                if (item.id != foods.last().id) {
                    Divider(color = Color.LightGray)
                }
            })
        }
    }
}

@Composable
fun FoodCard(
    food: Meal,
    modifier: Modifier,
    index: Long
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        shape = RectangleShape
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = food.food.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "${food.food.gramsEntered} гр",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(android.graphics.Color.parseColor("#99cd4e"))
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${food.food.protein}", fontSize = 16.sp, modifier = Modifier.padding(start = 10.dp))
                Text(text = "${food.food.fat}", fontSize = 16.sp, modifier = Modifier.padding(start = 0.dp))
                Text(text = "${food.food.carbs}", fontSize = 16.sp, modifier = Modifier.padding(start = 0.dp))
                Text(text = "${food.food.calories}", fontSize = 16.sp, modifier = Modifier.padding(start = 0.dp))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun Background(
    dismissState: DismissState,
    updateFraction: (Float) -> Unit) {

    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> Color.Transparent
            else -> PermanentGeraniumLake
        }
    )

    val alignment = Alignment.CenterEnd
    val icon = Icons.Default.Delete
    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1.2f
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        updateFraction(dismissState.progress.fraction)

        Icon(
            icon,
            contentDescription = "Localized description",
            modifier = Modifier.scale(scale)
        )
    }
}