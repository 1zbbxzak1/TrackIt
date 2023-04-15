package com.example.trackit.ui.workout.category

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.navigation.WorkoutEditTopBar
import com.example.trackit.ui.workout.Exercise
import com.example.trackit.ui.workout.WorkoutCategory
import kotlinx.coroutines.launch

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
    val dialogState = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { dialogState.value = true }) {
                Icon(Icons.Rounded.Add, contentDescription = "Add category")
            }
        },
        topBar = { WorkoutEditTopBar(title = "Выберите категорию", navigateBack = navigateBack) }
    ) {
        WorkoutCategoryBody(
            itemList = uiState.itemList,
            onCategorySelect,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteItem(it)
                }
            },
            modifier = modifier.padding(top = 8.dp)
        )

        if (dialogState.value) {
            AddCategoryDialog(
                "Добавление категории",
                "Название категории:",
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

@Composable
private fun WorkoutCategoryBody(
    itemList: List<WorkoutCategory>, onClick: (Int) -> Unit,
    onDelete: (WorkoutCategory) -> Unit,
    modifier: Modifier = Modifier
){
    WorkoutCategoryList(itemList = itemList, onClick, onDelete, modifier = modifier)
}

@Composable
private fun WorkoutCategoryList(
    itemList: List<WorkoutCategory>, onClick: (Int) -> Unit,
    onDelete: (WorkoutCategory) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)){
        items(itemList){item ->
            WorkoutCategoryItem(item = item, onClick, onDelete)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WorkoutCategoryItem(
    item: WorkoutCategory, onClick: (Int) -> Unit,
    onDelete: (WorkoutCategory) -> Unit,
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

            IconButton(onClick = { onDelete(item) }) {
                Icon(Icons.Rounded.Delete, contentDescription = "Удалить категорию")
            }

            Icon(
                Icons.Rounded.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp, end = 12.dp))
        }
    }
}

@Composable
fun AddCategoryDialog(
    label: String,
    textFieldLabel: String,
    onAddCategory: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var categoryName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp)
            //color = MaterialTheme.colors.secondary
        ) {
            Box(
                contentAlignment = Alignment.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = label, style = MaterialTheme.typography.h5)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        modifier = Modifier.padding(32.dp),
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        label = { Text(textFieldLabel) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(){
                        Row {
                            Button(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(30.dp)
                            ) {
                                   Text(text = "Отмена", style = MaterialTheme.typography.h6)
                            }
                            
                            Spacer(modifier = Modifier.width(10.dp))
                            
                            Button(onClick = {
                                onAddCategory(categoryName)
                                onDismiss()
                                },
                                shape = RoundedCornerShape(30.dp),
                                enabled = categoryName.isNotBlank()
                            ) {
                                Text(text = "Добавить", style = MaterialTheme.typography.h6)
                            }
                        }
                    }
                }
            }
        }
    }
}