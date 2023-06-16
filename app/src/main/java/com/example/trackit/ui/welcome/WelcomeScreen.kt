package com.example.trackit.ui.welcome

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.R
import com.example.trackit.activity.MainActivity
import com.example.trackit.data.weight.WeightEntry
import com.example.trackit.data.weight.WeightViewModel
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.theme.AndroidGreen
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.TrackItTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    weightViewModel: WeightViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val context = LocalContext.current
    val currentPage = remember { mutableStateOf(WelcomePage.Gender) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val coroutineScope = rememberCoroutineScope()

    val selectedGender = remember { mutableStateOf<Gender?>(null) }
    val selectedAge = remember { mutableStateOf(TextFieldValue("")) }
    val selectedHeight = remember { mutableStateOf(TextFieldValue("")) }
    val selectedWeight = remember { mutableStateOf(TextFieldValue("")) }

    val onClick: () -> Unit
    val enabled: Boolean
    val toastText: String

    when(currentPage.value){
        WelcomePage.Gender -> {
            onClick = {
                currentPage.value = getNextPage(currentPage.value)
            }
            enabled = selectedGender.value != null
            toastText = "Выберите пол"
        }
        WelcomePage.Age -> {
            onClick = {
                currentPage.value = getNextPage(currentPage.value)
            }
            enabled = selectedAge.value.text.isNotBlank() && selectedAge.value.text.toInt() < 123
            toastText = "Введите ваш возраст"
        }
        WelcomePage.Height -> {
            onClick = {
                currentPage.value = getNextPage(currentPage.value)
            }
            enabled = selectedHeight.value.text.isNotBlank() && selectedHeight.value.text.toInt() < 300
            toastText = "Введите ваш рост"
        }
        WelcomePage.Weight -> {
            onClick = {
                coroutineScope.launch {
                    weightViewModel.insertWeight(
                        WeightEntry(
                            0,
                            LocalTime.now(),
                            LocalDate.now(),
                            selectedWeight.value.text.toDouble()
                        )
                    )
                }

                navigateToMainActivity(
                    context,
                    getProfileData(
                        selectedGender.value!!,
                        selectedAge.value,
                        selectedHeight.value,
                        selectedWeight.value
                    )
                )
            }
            enabled = selectedWeight.value.text.isNotBlank() && selectedWeight.value.text.toDoubleOrNull()!! < 600
            toastText = "Введите ваш вес"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        WelcomeTopBar(Modifier.padding(horizontal = 10.dp), currentPage.value)

        Spacer(modifier = Modifier.height(70.dp))

        when(currentPage.value){
            WelcomePage.Gender -> GenderSelection(
                onSelected = {selectedGender.value = it}
            )
            WelcomePage.Age -> PageWithTextField(
                onSelected = { selectedAge.value = it },
                onKeyboardAction = {
                    if (enabled){
                        currentPage.value = getNextPage(currentPage.value)
                    }
                    else
                        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                },
                label = "Сколько вам лет?",
                imeAction = ImeAction.Next
            )
            WelcomePage.Height -> PageWithTextField(
                onSelected = { selectedHeight.value = it },
                onKeyboardAction = {
                    if (enabled){
                        currentPage.value = getNextPage(currentPage.value)
                    }
                    else
                        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                },
                label = "Какой у вас рост?",
                caption = "см",
                imeAction = ImeAction.Next
            )
            WelcomePage.Weight -> PageWithTextField(
                onSelected = { selectedWeight.value = it },
                onKeyboardAction = {
                    if (enabled) {
                        keyboardController?.hide()

                        coroutineScope.launch {
                            weightViewModel.insertWeight(
                                WeightEntry(
                                    0,
                                    LocalTime.now(),
                                    LocalDate.now(),
                                    selectedWeight.value.text.toDouble()
                                )
                            )
                        }

                        navigateToMainActivity(
                            context,
                            getProfileData(
                                selectedGender.value!!,
                                selectedAge.value,
                                selectedHeight.value,
                                selectedWeight.value
                            )
                        )
                    }
                    else
                        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                },
                label = "Какой у вас вес?",
                caption = "кг",
                imeAction = ImeAction.Done
            )
        }

        Spacer(Modifier.weight(1f))

        WelcomeButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 96.dp),
            page = currentPage.value,
            toastText = toastText,
            enabled = enabled,
            onClick = onClick
        )
    }
}

private fun getProfileData(
    gender: Gender,
    ageTextFieldValue: TextFieldValue,
    heightTextFieldValue: TextFieldValue,
    weightTextFieldValue: TextFieldValue
) : WelcomeData = WelcomeData(gender, ageTextFieldValue.text.toInt(), heightTextFieldValue.text.toInt(), weightTextFieldValue.text.toDouble())

private fun getNextPage(currentPage: WelcomePage) : WelcomePage{
    val values = WelcomePage.values()
    return values[currentPage.ordinal + 1]
}

private enum class WelcomePage{
    Gender, Age, Height, Weight
}

@Composable
private fun WelcomeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    toastText: String = "",
    enabled: Boolean = false,
    page: WelcomePage
){
    val text = if (page == WelcomePage.Weight) "Начать" else "Продолжить"
    val textColor = if (enabled) AndroidGreen else Color.White
    val context = LocalContext.current

    Button(
        modifier = modifier,
        onClick = { if (enabled) onClick() else Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()},
        colors = ButtonDefaults.buttonColors(backgroundColor = Arsenic),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = textColor
            )
        )
    }
}

@Composable
private fun WelcomeTopBar(
    modifier: Modifier = Modifier,
    currentPage: WelcomePage
){

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TopBarCircle(highlighted = WelcomePage.Gender == currentPage)

            Icon(painterResource(id = R.drawable.welcome_line), contentDescription = null)

            TopBarCircle(highlighted = WelcomePage.Age == currentPage)

            Icon(painterResource(id = R.drawable.welcome_line), contentDescription = null)

            TopBarCircle(highlighted = WelcomePage.Height == currentPage)

            Icon(painterResource(id = R.drawable.welcome_line), contentDescription = null)

            TopBarCircle(highlighted = WelcomePage.Weight == currentPage)
        }
    }
}

@Composable
private fun TopBarCircle(
    modifier: Modifier = Modifier,
    highlighted: Boolean
){
    val icon = if (highlighted) R.drawable.highlighted_circle else R.drawable.circle
    val iconColor = if (highlighted) AndroidGreen else Arsenic

    Icon(
        modifier = Modifier.requiredSize(30.dp),
        painter = painterResource(id = icon),
        contentDescription = null,
        tint = iconColor
    )
}

data class WelcomeData(
    val gender: Gender,
    val age: Int,
    val height: Int,
    val weight: Double
)

// Функция для перехода на другое активити
private fun navigateToMainActivity(context: Context, profileData: WelcomeData) {
    val intent = Intent(context, MainActivity::class.java)
    // Передача данных в MainActivity для дальнейшего использования в профиле
    intent.putExtra("gender", profileData.gender.text)
    intent.putExtra("age", profileData.age)
    intent.putExtra("height", profileData.height)
    context.startActivity(intent)
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun WelcomeScreenPreview(){
    TrackItTheme {
        WelcomeScreen(Modifier.padding(horizontal = 10.dp))
    }
}