package com.example.trackit.ui.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.trackit.data.ExerciseType
import com.example.trackit.ui.theme.AntiFlashWhite
import java.time.Duration

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExerciseDialog(
    selectedEntity: WorkoutEntity,
    onEntity: (WorkoutEntity) -> Unit,
    onDismiss: () -> Unit,
) {
    var duration = ""
    var weight = ""
    var repeatCount = ""
    var approachCount = ""

    when (selectedEntity.exercise){
        is StrengthExercise -> {
            weight = selectedEntity.exercise.weight.toString()
            repeatCount = selectedEntity.exercise.repeatCount.toString()
            approachCount = selectedEntity.exercise.approachCount.toString()
        }
        is CardioExercise -> {
            duration = selectedEntity.exercise.time.toMinutes().toString()
        }

    }
    var durationField by remember { mutableStateOf(duration) }
    var weightField by remember { mutableStateOf(weight) }
    var repeatCountField by remember { mutableStateOf(repeatCount) }
    var approachCountField by remember { mutableStateOf(approachCount) }

//    val keyboardController = LocalSoftwareKeyboardController.current
//    val focusRequester = remember { FocusRequester() }
//    val focusManager = LocalFocusManager.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column() {
                    when(selectedEntity.exercise){
                        is StrengthExercise -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Силовая", style = MaterialTheme.typography.h5)

                                val keyboardController = LocalSoftwareKeyboardController.current
                                val focusManager = LocalFocusManager.current

                                //вес(кг)
                                TextField(
                                    modifier = Modifier
                                        .padding(32.dp),
                                    value = weightField,
                                    onValueChange = { weightField = it },
                                    label = { Text("Вес (кг)") },
                                    maxLines = 1,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                    )
                                )

                                // повторения
                                TextField(
                                    modifier = Modifier
                                        .padding(32.dp),
                                    value = repeatCountField,
                                    onValueChange = { repeatCountField = it },
                                    label = { Text("Кол-во повторений:") },
                                    maxLines = 1,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                    )
                                )

                                // подходы
                                TextField(
                                    modifier = Modifier
                                        .padding(32.dp),
                                    value = approachCountField,
                                    onValueChange = { approachCountField = it },
                                    label = { Text("Кол-во подходов:") },
                                    maxLines = 1,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(onDone = {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                    )
                                )
                            }
                        }
                        is CardioExercise -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Кардио", style = MaterialTheme.typography.h5)

                                val keyboardController = LocalSoftwareKeyboardController.current
                                val focusManager = LocalFocusManager.current


                                // длительность (мин)
                                TextField(
                                    modifier = Modifier
                                        .padding(32.dp),
                                    value = durationField,
                                    onValueChange = { durationField = it },
                                    label = { Text("Длительность (мин):") },
                                    maxLines = 1,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                    )
                                )
                            }
                        }
                    }

                    // кнопки отмены и добавления
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = onDismiss,
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(text = "Отмена", style = MaterialTheme.typography.h6)
                        }

                        Spacer(modifier = Modifier.width(15.dp))

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onEntity(
                                    WorkoutEntity(
                                        selectedEntity.id,
                                        selectedEntity.name,
                                        when (selectedEntity.exercise) {
                                            is CardioExercise -> CardioExercise(
                                                selectedEntity.name,
                                                Duration.ofMinutes(durationField.toLong())
                                            )
                                            is StrengthExercise -> StrengthExercise(
                                                selectedEntity.name,
                                                weightField.toFloat(),
                                                repeatCountField.toInt(),
                                                approachCountField.toInt()
                                            )
                                        },
                                        selectedEntity.category,
                                        selectedEntity.date,
                                        selectedEntity.completed
                                    )
                                )
                                onDismiss()
                            },
                            shape = RoundedCornerShape(30.dp),
                            enabled =
                            when (selectedEntity.exercise){
                                is StrengthExercise -> {
                                    selectedEntity.name.isNotBlank() &&
                                            weightField.isNotBlank()
                                            && repeatCountField.isNotBlank()
                                            && approachCountField.isNotBlank()
                                }
                                is CardioExercise -> {
                                    selectedEntity.name.isNotBlank() &&
                                            durationField.isNotBlank()
                                }
                            }
                        ) {
                            Text(text = "Готово", style = MaterialTheme.typography.h6)
                        }

                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }
            }
        }
    }
}





@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddExerciseDialog(
    selectedExercise: Exercise,
    onAddExercise: (Exercise) -> Unit,
    onDismiss: () -> Unit,
    duration: String = "",
    weight: String = "",
    repeatCount: String = "",
    approachCount: String = ""
) {
    var durationField by remember { mutableStateOf(duration) }
    var weightField by remember { mutableStateOf(weight) }
    var repeatCountField by remember { mutableStateOf(repeatCount) }
    var approachCountField by remember { mutableStateOf(approachCount) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column() {
                    when(selectedExercise){
                        is StrengthExercise -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Силовая", style = MaterialTheme.typography.h5)

                                val keyboardController = LocalSoftwareKeyboardController.current
                                val focusManager = LocalFocusManager.current

                                //вес(кг)
                                TextField(
                                    modifier = Modifier.padding(32.dp),
                                    value = weightField,
                                    onValueChange = { weightField = it },
                                    label = { Text("Вес:") },
                                    maxLines = 1,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                    )
                                )

                                // повторения
                                TextField(
                                    modifier = Modifier.padding(32.dp),
                                    value = repeatCountField,
                                    onValueChange = { repeatCountField = it },
                                    label = { Text("Кол-во повторений:") },
                                    maxLines = 1,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                    )
                                )

                                // подходы
                                TextField(
                                    modifier = Modifier.padding(32.dp),
                                    value = approachCountField,
                                    onValueChange = { approachCountField = it },
                                    label = { Text("Кол-во подходов:") },
                                    maxLines = 1,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                    )
                                )
                            }
                        }
                        is CardioExercise -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Кардио", style = MaterialTheme.typography.h5)

                                val keyboardController = LocalSoftwareKeyboardController.current
                                val focusManager = LocalFocusManager.current

                                // длительность (мин)
                                TextField(
                                    modifier = Modifier.padding(32.dp),
                                    value = durationField,
                                    onValueChange = { durationField = it },
                                    label = { Text("Длительность (мин):") },
                                    maxLines = 1,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                    )
                                )
                            }
                        }
                    }

                    // кнопки отмены и добавления
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = onDismiss,
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(text = "Отмена", style = MaterialTheme.typography.h6)
                        }

                        Spacer(modifier = Modifier.width(15.dp))

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onAddExercise(
                                    when (selectedExercise) {
                                        is CardioExercise -> CardioExercise(
                                            selectedExercise.name,
                                            Duration.ofMinutes(durationField.toLong())
                                        )
                                        is StrengthExercise -> StrengthExercise(
                                            selectedExercise.name,
                                            weightField.toFloat(),
                                            repeatCountField.toInt(),
                                            approachCountField.toInt()
                                        )
                                    }
                                )
                                onDismiss()
                            },
                            shape = RoundedCornerShape(30.dp),
                            enabled =
                            when (selectedExercise){
                                is StrengthExercise -> {
                                    selectedExercise.name.isNotBlank() &&
                                            weightField.isNotBlank()
                                            && repeatCountField.isNotBlank()
                                            && approachCountField.isNotBlank()
                                }
                                is CardioExercise -> {
                                    selectedExercise.name.isNotBlank() &&
                                            durationField.isNotBlank()
                                }
                            }
                        ) {
                            Text(text = "Добавить", style = MaterialTheme.typography.h6)
                        }

                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CreateNewExerciseDialog(
    onAddExercise: (Exercise) -> Unit,
    onDismiss: () -> Unit
) {
    var exerciseName by remember { mutableStateOf("") }
    var currentExercise by remember { mutableStateOf(ExerciseType.Strength) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row() {
                        Text(text = "Добавление упражнения", style = MaterialTheme.typography.h5)
                    }

                    TextField(
                        modifier = Modifier.padding(32.dp),
                        value = exerciseName,
                        onValueChange = { exerciseName = it },
                        label = { Text("Название упражнения:") },
                        maxLines = 1,
                        singleLine = true,
                    )

                    Card(
                        border = BorderStroke(1.dp, MaterialTheme.colors.primaryVariant),
                        shape = RoundedCornerShape(20.dp),
                        backgroundColor = AntiFlashWhite
                    ) {
                        StateSwitch(
                            state1 = ExerciseType.Strength,
                            state2 = ExerciseType.Cardio,
                            currentState = currentExercise,
                            onStateChanged = {currentExercise = it}
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = onDismiss,
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text(text = "Отмена", style = MaterialTheme.typography.h6)
                        }

                        Spacer(modifier = Modifier.width(15.dp))

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onAddExercise(
                                    when (currentExercise) {
                                        ExerciseType.Cardio -> CardioExercise(
                                            exerciseName,
                                            Duration.ZERO
                                        )
                                        ExerciseType.Strength -> StrengthExercise(
                                            exerciseName,
                                            0.0f, 0, 0
                                        )
                                    }
                                )
                                onDismiss()
                            },
                            shape = RoundedCornerShape(30.dp),
                            enabled = exerciseName.isNotBlank()
                        ) {
                            Text(text = "Добавить", style = MaterialTheme.typography.h6)
                        }

                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StateSwitch(
    state1: ExerciseType,
    state2: ExerciseType,
    currentState: ExerciseType,
    onStateChanged: (ExerciseType) -> Unit
) {
    Row(
        Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            backgroundColor = if (currentState == state1) MaterialTheme.colors.primaryVariant else AntiFlashWhite,
            modifier = Modifier
                .clickable {
                    val newState = if (currentState == state1) state2 else state1
                    onStateChanged(newState)
                }
                .size(width = 90.dp, height = 40.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Силовая",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.width(15.dp))

        Card(
            backgroundColor = if (currentState == state2) MaterialTheme.colors.primaryVariant else AntiFlashWhite,
            modifier = Modifier
                .clickable {
                    val newState = if (currentState == state1) state2 else state1
                    onStateChanged(newState)
                }
                .size(width = 90.dp, height = 40.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Кардио",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}