package com.example.trackit.ui.workout.category

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.FloatingButton
import com.example.trackit.data.Screen
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.navigation.WorkoutEditTopBar
import com.example.trackit.ui.workout.WorkoutCategory

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WorkoutCategoryScreen(
    onCategorySelect: (Int) -> Unit,
    navigateBack: () -> Unit,
    viewModel: WorkoutCategoryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
){
    val uiState by viewModel.workoutCategoryUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingButton(currentRoute = Screen.WorkoutCategory.name, onClick = {})
        },
        topBar = { WorkoutEditTopBar(title = "Выберите категорию", navigateBack = navigateBack) }
    ) {
        WorkoutCategoryBody(
            itemList = uiState.itemList,
            onCategorySelect,
            modifier = modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun WorkoutCategoryBody(
    itemList: List<WorkoutCategory>, onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    WorkoutCategoryList(itemList = itemList, onClick, modifier = modifier)
}

@Composable
private fun WorkoutCategoryList(
    itemList: List<WorkoutCategory>, onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)){
        items(itemList){item ->
            WorkoutCategoryItem(item = item, onClick)
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
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.name, style = MaterialTheme.typography.h5, modifier = Modifier.weight(1f))
            Icon(
                Icons.Rounded.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp, end = 12.dp))
        }
    }
}