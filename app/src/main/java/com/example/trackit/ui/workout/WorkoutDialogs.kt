package com.example.trackit.ui.workout

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.trackit.data.ExerciseType
import com.example.trackit.data.workout.CardioExercise
import com.example.trackit.data.workout.Exercise
import com.example.trackit.data.workout.StrengthExercise
import com.example.trackit.ui.theme.*
import java.time.Duration

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExerciseDialog(
    selectedExercise: Exercise,
    onAddExercise: (Exercise) -> Unit,
    onDismiss: () -> Unit,
) {
    var durationField by remember { mutableStateOf("") }
    var weightField by remember { mutableStateOf("") }
    var repeatCountField by remember { mutableStateOf("") }
    var approachCountField by remember { mutableStateOf("") }

    when (selectedExercise){
        is StrengthExercise -> {
            weightField = if (selectedExercise.weight != 0) selectedExercise.weight.toString() else ""
            repeatCountField = if (selectedExercise.repeatCount != 0) selectedExercise.repeatCount.toString() else ""
            approachCountField = if (selectedExercise.approachCount != 0) selectedExercise.approachCount.toString() else ""
        }
        is CardioExercise -> {
            durationField = if (selectedExercise.time.toMinutes().toInt() != 0) selectedExercise.time.toMinutes().toString() else ""
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(25.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    when(selectedExercise){
                        is StrengthExercise -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(bottom = 22.dp)
                            ) {
                                Text(text = "Силовая", style = DialogTextStyle)

                                val keyboardController = LocalSoftwareKeyboardController.current
                                val focusManager = LocalFocusManager.current

                                Spacer(Modifier.height(13.dp))

                                //вес(кг)
                                DialogTextField(
                                    value = weightField,
                                    onValueChange = { weightField = it },
                                    label = "Вес",
                                    caption = "кг",
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                    ),
                                    modifier = Modifier
                                        .padding(vertical = 7.dp)
                                        .height(48.dp)
                                )

                                // повторения
                                DialogTextField(
                                    value = repeatCountField,
                                    onValueChange = { repeatCountField = it },
                                    label = "Повторения*",
                                    caption = "кол-во",
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                    ),
                                    modifier = Modifier
                                        .padding(vertical = 7.dp)
                                        .height(48.dp)
                                )

                                // подходы
                                DialogTextField(
                                    value = approachCountField,
                                    onValueChange = { approachCountField = it },
                                    label = "Подходы*",
                                    caption = "кол-во",
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                    ),
                                    modifier = Modifier
                                        .padding(vertical = 7.dp)
                                        .height(48.dp)
                                )

                                Text(
                                    text = "*  обязательные поля",
                                    style = TextFieldLabelTextStyle,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp)
                                )
                            }
                        }
                        is CardioExercise -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(bottom = 32.dp)
                            ) {
                                Text(text = "Кардио", style = DialogTextStyle)

                                val keyboardController = LocalSoftwareKeyboardController.current
                                val focusManager = LocalFocusManager.current

                                Spacer(modifier = Modifier.height(20.dp))

                                // длительность (мин)
                                DialogTextField(
                                    value = durationField,
                                    onValueChange = { durationField = it },
                                    label = "Время",
                                    caption = "мин",
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                    ),
                                    modifier = Modifier.height(60.dp)
                                )
                            }
                        }
                    }

                    // кнопки отмены и добавления
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        AddDeleteButton(
                            text = "Отмена",
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Arsenic),
                            modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.width(5.dp))

                        val enabled = when (selectedExercise){
                            is StrengthExercise -> {
                                selectedExercise.name.isNotBlank() &&
                                        repeatCountField.isNotBlank() && repeatCountField.toInt() > 0
                                        && approachCountField.isNotBlank() && approachCountField.toInt() > 0
                            }
                            is CardioExercise -> {
                                selectedExercise.name.isNotBlank() &&
                                        durationField.isNotBlank() && durationField.toInt() > 0
                            }
                        }

                        val context = LocalContext.current

                        AddDeleteButton(
                            text = "Готово",
                            onClick = {
                                if (enabled){
                                    onAddExercise(
                                        when (selectedExercise) {
                                            is CardioExercise -> CardioExercise(
                                                name = selectedExercise.name,
                                                time = Duration.ofMinutes(durationField.toLong())
                                            )
                                            is StrengthExercise -> StrengthExercise(
                                                name = selectedExercise.name,
                                                weight = if (weightField.isBlank()) 0 else weightField.toInt(),
                                                repeatCount = repeatCountField.toInt(),
                                                approachCount = approachCountField.toInt()
                                            )
                                        }
                                    )
                                    onDismiss()
                                }
                                else Toast.makeText(context, "Введите необходимые данные", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Arsenic,
                                contentColor = if (enabled) AndroidGreen else Color.White
                            ),
                            modifier = Modifier.weight(1f))
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
            shape = RoundedCornerShape(25.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = "Добавление упражнения", style = DialogTextStyle, maxLines = 1)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    DialogTextField(
                        value = exerciseName,
                        onValueChange = { exerciseName = it },
                        label = "Название упражнения",
                        modifier = Modifier.height(60.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    StateSwitch(
                        state1 = ExerciseType.Strength,
                        state2 = ExerciseType.Cardio,
                        currentState = currentExercise,
                        onStateChanged = {currentExercise = it}
                    )

                    Spacer(Modifier.height(32.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AddDeleteButton(
                            text = "Отмена",
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Arsenic),
                            modifier = Modifier.weight(1f))

                        Spacer(modifier = Modifier.width(5.dp))

                        val enabled = exerciseName.isNotBlank()
                        val context = LocalContext.current

                        AddDeleteButton(
                            text = "Готово",
                            onClick = {
                                if (enabled){
                                    onAddExercise(
                                        when (currentExercise) {
                                            ExerciseType.Cardio -> CardioExercise(
                                                name = exerciseName,
                                                time = Duration.ZERO
                                            )
                                            ExerciseType.Strength -> StrengthExercise(
                                                name = exerciseName,
                                                weight = 0, repeatCount = 0, approachCount = 0
                                            )
                                        }
                                    )
                                    onDismiss()
                                }
                                else Toast.makeText(context, "Введите название упражнения", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Arsenic,
                                contentColor = if (enabled) AndroidGreen else Color.White
                            ),
                            modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun AddCategoryDialog(
    onAddCategory: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var categoryName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(25.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Добавление категории", style = DialogTextStyle, maxLines = 1)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    DialogTextField(
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        label = "Название категории",
                        modifier = Modifier.height(60.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        AddDeleteButton(
                            text = "Отмена",
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Arsenic),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        val enabled = categoryName.isNotBlank()
                        val context = LocalContext.current

                        AddDeleteButton(
                            text = "Готово",
                            onClick = {
                                if (enabled){
                                    onAddCategory(categoryName)
                                    onDismiss()
                                }
                                else Toast.makeText(context, "Введите название категории", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Arsenic,
                                contentColor = if (enabled) AndroidGreen else Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DialogTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    caption: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    placeHolder: String = "",
    shape: Shape = RoundedCornerShape(20.dp),
    modifier: Modifier = Modifier
){
    BasicTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = {onValueChange(it)},
        textStyle = TextFieldTextStyle,
        maxLines = 1,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    ) {innerTextField ->
        Card(
            shape = shape,
            backgroundColor = Color.White,
            elevation = 6.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = label, style = TextFieldLabelTextStyle,
                modifier = Modifier.padding(start = 20.dp)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()){
                            Text(
                                text = placeHolder,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Light,
                                color = BrightGray
                            )
                        }
                        innerTextField()
                    }
                    Text(text = caption, style = TextFieldTextStyle)
                }
            }
        }
    }
}

@Composable
fun AddDeleteButton(
    text: String,
    onClick: () -> Unit,
    colors: ButtonColors,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        colors = colors,
        enabled = enabled,
        modifier = modifier.height(40.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.offset(0.dp, (-4).dp)
        )
    }
}

@Composable
fun StateSwitch(
    state1: ExerciseType,
    state2: ExerciseType,
    currentState: ExerciseType,
    onStateChanged: (ExerciseType) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(15.dp),
        modifier = modifier,
        color = Color.White
    ) {
        Row(
            Modifier.padding(3.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                backgroundColor = if (currentState == state1) MaterialTheme.colors.primaryVariant else Color.White,
                modifier = Modifier
                    .clickable(remember { MutableInteractionSource() }, null) {
                        val newState = if (currentState == state1) state2 else state1
                        onStateChanged(newState)
                    }
                    .weight(1f),
                shape = RoundedCornerShape(15.dp),
                elevation = if (currentState == state1) 2.dp else 0.dp
            ) {
                Text(
                    text = "Силовая",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = if (currentState == state1) Color.White else Arsenic
                )
            }
            Card(
                backgroundColor = if (currentState == state2) MaterialTheme.colors.primaryVariant else Color.White,
                modifier = Modifier
                    .clickable(remember { MutableInteractionSource() }, null) {
                        val newState = if (currentState == state1) state2 else state1
                        onStateChanged(newState)
                    }
                    .weight(1f),
                shape = RoundedCornerShape(15.dp),
                elevation = if (currentState == state2) 2.dp else 0.dp
            ) {
                Text(
                    text = "Кардио",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 8.dp),
                    fontSize = 20.sp,
                    color = if (currentState == state2) Color.White else Arsenic
                )
            }
        }
    }

}