package com.example.trackit.ui.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trackit.FloatingButton
import com.example.trackit.R
import com.example.trackit.data.Screen
import com.example.trackit.data.workout.CardioExercise
import com.example.trackit.data.workout.StrengthExercise
import com.example.trackit.data.workout.WorkoutCategory
import com.example.trackit.data.workout.WorkoutEntity
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.navigation.BottomBar
import com.example.trackit.ui.theme.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.util.*

@Composable
fun WorkoutPage(
    navigateToEntry: () -> Unit,
    selectedDate: LocalDate = LocalDate.now(),
    modifier: Modifier = Modifier,
    viewModel: WorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    viewModel.updateSelectedDate(selectedDate)
    val workoutUiState by viewModel.workoutUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var editDialogState by remember { mutableStateOf(false) }
    var selectedEntity by remember {
        mutableStateOf(
            WorkoutEntity(
                0,
                "",
                CardioExercise("", Duration.ZERO),
                WorkoutCategory(0, "", mutableListOf()),
                LocalDate.now(),
                false
            )
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingButton(Screen.Workout.name, onClick = { navigateToEntry() })
        },
    ) {
        Column(
            modifier = modifier.padding(bottom = it.calculateBottomPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Surface(
                color = MaterialTheme.colors.primaryVariant,
                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                modifier = Modifier.height(70.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(painterResource(id = R.drawable.workout_label), contentDescription = null, tint = Color.Unspecified)
                }
            }

            WorkoutBody(
                selectedDate,
                itemList = workoutUiState.itemList,
                onDismiss = {item -> coroutineScope.launch {
                    viewModel.deleteItem(item)
                }},
                onCheckedChange = {coroutineScope.launch {
                    viewModel.updateItem(it)
                }},
                onEdit = {
                    selectedEntity = it
                    editDialogState = true
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally))
        }
    }

    if (editDialogState){
        ExerciseDialog(
            selectedExercise = selectedEntity.exercise,
            onAddExercise = {exercise ->
                coroutineScope.launch {
                    viewModel.updateItem(WorkoutEntity(
                        selectedEntity.id,
                        selectedEntity.name,
                        exercise,
                        selectedEntity.category,
                        selectedEntity.date,
                        selectedEntity.completed
                    )
                    )
                }
            },
            onDismiss = { editDialogState = false }
        )
    }
}

@Composable
private fun WorkoutBody(
    selectedDate: LocalDate,
    itemList: List<WorkoutEntity>,
    onDismiss: (WorkoutEntity) -> Unit,
    onCheckedChange: (WorkoutEntity) -> Unit,
    onEdit: (WorkoutEntity) -> Unit,
    modifier: Modifier = Modifier
){
    WorkoutList(selectedDate, itemList, onDismiss, onCheckedChange, onEdit, modifier)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun WorkoutList(
    selectedDate: LocalDate,
    itemList: List<WorkoutEntity>,
    onDismiss: (WorkoutEntity) -> Unit,
    onCheckedChange: (WorkoutEntity) -> Unit,
    onEdit: (WorkoutEntity) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item{
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier.height(25.dp))
                Text(text = selectedDate.dayOfMonth.toString() + " " + selectedDate.month
                    .getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
                    .lowercase()
                    .replaceFirstChar { it.titlecase() } + " " + selectedDate.year,
                    style = MaterialTheme.typography.h4,
                    color = Arsenic)
                Spacer(modifier.height(20.dp))
            }
        }

        if (itemList.isEmpty()){
            item(){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(243.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.empty_workout_page),
                            contentDescription = null,
                            tint = EmptyColor
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = stringResource(id = R.string.empty_workout_page),
                            textAlign = TextAlign.Center,
                            style = EmptyCaption,
                            color = EmptyColor,
                        )
                    }
                }
            }
        }
        else{
            items(items = itemList, key = { item -> item.id }, itemContent = {item ->
                val dismissThreshold = 0.25f
                val currentFraction = remember { mutableStateOf(0f) }

                var willDismissDirection: DismissDirection? by remember {
                    mutableStateOf(null)
                }
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        when(it){
                            DismissValue.DismissedToStart -> {
                                if (currentFraction.value >= dismissThreshold && currentFraction.value < 1.0f) {
                                    onDismiss(item)
                                }
                                currentFraction.value >= dismissThreshold && currentFraction.value < 1.0f
                            }
                            else -> false
                        }
                    }
                )

                willDismissDirection = when(dismissState.targetValue){
                    DismissValue.Default -> null
                    else ->DismissDirection.EndToStart
                }

                val haptic = LocalHapticFeedback.current
                LaunchedEffect(key1 = willDismissDirection, block = {
                    if (willDismissDirection != null) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                })

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = {
                        FractionalThreshold(dismissThreshold)
                    },
                    modifier = Modifier
                        .animateItemPlacement(),
                    background = {
                        SwipeBackground(dismissState = dismissState) { currentFraction.value = it }
                    },
                    dismissContent = {
                        WorkoutItem(item = item, onCheckedChange, onEdit)
                    }
                )
            })
        }

        item {
            Spacer(modifier.height(100.dp))
        }
    }
}

@Composable
private fun WorkoutItem(
    item: WorkoutEntity,
    onCheckedChange: (WorkoutEntity) -> Unit,
    onEdit: (WorkoutEntity) -> Unit,
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.workout_icon
){
    var expanded by remember {
        mutableStateOf(false)
    }

    Card(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp)
        .clickable(remember { MutableInteractionSource() }, null) { expanded = !expanded },
        elevation = 4.dp,
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                CustomCheckBox(
                    checked = item.completed,
                    onCheckedChange = {
                        onCheckedChange(WorkoutEntity(item.id, item.name, item.exercise, item.category, item.date, it))
                    },
                    item.category.icon,
                    modifier = Modifier.padding(start = 10.dp, end = 5.dp)
                )

                Text(text = item.name, style = WorkoutCaption, fontSize = 18.sp, color = Arsenic, modifier = Modifier.weight(1f))

                if (!expanded){
                    Card (
                        modifier = Modifier
                            .clickable(
                                remember { MutableInteractionSource() },
                                null
                            ) { onEdit(item) }
                            .padding(horizontal = 10.dp),
                        shape = RoundedCornerShape(15.dp),
                        backgroundColor = Color.White,
                        border = BorderStroke(1.dp, Arsenic),
                        elevation = 0.dp
                    ){
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            when (item.exercise){
                                is StrengthExercise -> {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("${item.exercise.weight}",
                                            style = WorkoutCaption, color = Arsenic)
                                        Icon(painterResource(R.drawable.x), contentDescription = null,
                                            modifier = Modifier.padding(horizontal = 3.dp))
                                        Text("${item.exercise.repeatCount}",
                                            style = WorkoutCaption, color = Arsenic)
                                        Icon(painterResource(R.drawable.x), contentDescription = null,
                                            modifier = Modifier.padding(horizontal = 3.dp))
                                        Text("${item.exercise.approachCount}",
                                            style = WorkoutCaption, color = Arsenic)
                                    }
                                }
                                is CardioExercise -> {
                                    Text("${item.exercise.time.toMinutes()} мин",
                                        style = WorkoutCaption, color = Arsenic)
                                }
                            }
                        }
                    }
                }
                Icon(
                    if (!expanded) Icons.Rounded.KeyboardArrowDown
                    else Icons.Rounded.KeyboardArrowUp,
                    contentDescription = null,
                    tint = Arsenic,
                )
            }

            AnimatedVisibility(visible = expanded){
                Card(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                        .background(BrightGray)
                        .clickable { onEdit(item) },
                    elevation = 2.dp,
                    shape = RoundedCornerShape(15.dp),
                    backgroundColor = Color.White,
                    border = BorderStroke(1.dp, Arsenic)
                ) {
                    Column(
                        Modifier
                            .fillMaxSize(1f)
                            .padding(horizontal = 6.dp, vertical = 8.dp)) {
                        Text(text = "Категория: ${item.category.name}",
                            style = WorkoutCaption
                        )

                        Spacer(Modifier.height(4.dp))
                        val resources = LocalContext.current.resources

                        when (item.exercise){
                            is StrengthExercise -> {
                                val weight = item.exercise.weight
                                val repeatCount = item.exercise.repeatCount
                                val approachCount = item.exercise.approachCount

                                Row(
                                    modifier = Modifier.fillMaxWidth(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Text(
                                        text = "$weight кг",
                                        style = WorkoutCaption
                                    )

                                    Icon(painterResource(R.drawable.x), contentDescription = null,
                                        modifier = Modifier.padding(horizontal = 5.dp))

                                    Text(
                                        text = resources.getQuantityString(R.plurals.repeats, repeatCount, repeatCount),
                                        style = WorkoutCaption
                                    )

                                    Icon(painterResource(R.drawable.x), contentDescription = null,
                                        modifier = Modifier.padding(horizontal = 5.dp))

                                    Text(
                                        text = resources.getQuantityString(R.plurals.approaches, approachCount, approachCount),
                                        style = WorkoutCaption
                                    )
                                }
                            }
                            is CardioExercise -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = resources.getQuantityString(R.plurals.minutes,
                                            item.exercise.time.toMinutes().toInt(), item.exercise.time.toMinutes().toInt()),
                                        style = WorkoutCaption
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun SwipeBackground(dismissState: DismissState, updateFraction: (Float) -> Unit) {

    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> MaterialTheme.colors.background
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

@Composable
fun CustomCheckBox(
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    icon: Int,
    modifier: Modifier = Modifier
){
    Button(
        onClick = {onCheckedChange(!checked)},
        border = if (checked) BorderStroke(2.dp, AndroidGreen) else BorderStroke(2.dp, Arsenic),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = if (checked) AndroidGreen else Color.White),
        modifier = modifier.size(50.dp)
    ){
        Icon(
            painterResource(id = if (checked) R.drawable.completed else icon),
            contentDescription = null,
            tint = Arsenic,
            modifier = Modifier
                .size(30.dp)
                .requiredSize(30.dp)
        )
    }
}