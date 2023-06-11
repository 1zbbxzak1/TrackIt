package com.example.trackit.ui.workout.category

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.data.workout.WorkoutCategory
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.navigation.WorkoutEditTopBar
import com.example.trackit.ui.theme.AndroidGreen
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.workout.AddCategoryDialog
import com.example.trackit.ui.workout.SwipeBackground
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutCategoryScreen(
    onCategorySelect: (Int) -> Unit,
    navigateBack: () -> Unit,
    viewModel: WorkoutCategoryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
){
    val uiState by viewModel.workoutCategoryUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val dialogState = remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    Box{
        Column {
            WorkoutEditTopBar(textState, navigateBack = navigateBack)

            Card(
                onClick = { dialogState.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = 8.dp,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Создать категорию",
                        modifier = Modifier.weight(1f),
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = Arsenic
                        )
                    )

                    Icon(
                        Icons.Rounded.Add, contentDescription = "Создать категорию",
                        tint = Arsenic, modifier = Modifier.size(40.dp)
                    )
                }
            }

            WorkoutCategoryBody(
                itemList = uiState.itemList,
                onCategorySelect,
                textState,
                onDelete = {
                    coroutineScope.launch {
                        viewModel.deleteItem(it)
                    }
                },
                modifier = modifier.padding(top = 8.dp)
            )

            if (dialogState.value) {
                AddCategoryDialog(
                    onAddCategory = { name ->
                        coroutineScope.launch {
                            viewModel.insertItem(WorkoutCategory(0, name, mutableListOf()))
                        }
                        dialogState.value = false
                    },
                    onDismiss = { dialogState.value = false }
                )
            }
        }
    }
}

@Composable
private fun WorkoutCategoryBody(
    itemList: List<WorkoutCategory>, onClick: (Int) -> Unit,
    textState: MutableState<TextFieldValue>,
    onDelete: (WorkoutCategory) -> Unit,
    modifier: Modifier = Modifier
){
    WorkoutCategoryList(itemList = itemList, onClick, textState, onDelete, modifier = modifier)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun WorkoutCategoryList(
    itemList: List<WorkoutCategory>, onClick: (Int) -> Unit,
    textState: MutableState<TextFieldValue>,
    onDelete: (WorkoutCategory) -> Unit,
    modifier: Modifier = Modifier
){
    var filteredItems: List<WorkoutCategory>

    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(18.dp)){
        val searchedText = textState.value.text.lowercase(Locale.getDefault())
        filteredItems = itemList.filter { item ->
            item.name.lowercase(Locale.getDefault()).contains(searchedText) ||
                    item.exercises.any { exercise ->
                        exercise.name.lowercase(Locale.getDefault()).contains(searchedText)
                    }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier.height(25.dp))
                Text(text = "Категории", style = MaterialTheme.typography.h4)
                Spacer(modifier.height(25.dp))
            }
        }

        items(filteredItems, key = { item -> item.id }, itemContent = {item ->
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
                                onDelete(item)
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
                    WorkoutCategoryItem(item = item, onClick)
                }
            )
        })

        item(){
            Spacer(modifier.height(100.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WorkoutCategoryItem(
    item: WorkoutCategory, onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        onClick = {onClick(item.id)},
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(start = 20.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = item.icon), contentDescription = null,
                tint = Arsenic, modifier = Modifier.size(40.dp).requiredSize(40.dp)
            )

            Spacer(Modifier.width(10.dp))

            Text(text = item.name, modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Arsenic
                )
            )

            Icon(
                Icons.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = AndroidGreen,
                modifier = Modifier
                    .size(width = 45.dp, height = 60.dp)
            )
        }
    }
}