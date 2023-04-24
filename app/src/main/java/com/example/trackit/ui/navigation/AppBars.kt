package com.example.trackit.ui.navigation

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackit.R
import com.example.trackit.calendar.ExpandableCalendar
import java.time.LocalDate
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.trackit.data.Screen
import com.example.trackit.ui.theme.AndroidGreen
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.BrightGray
import com.example.trackit.ui.theme.TrackItTheme

@Composable
fun BottomBar(
    currentDate: LocalDate = LocalDate.now(),
    barState: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    navController: NavController,
    currentScreen: Screen
){
    var calendarExpanded by remember {
        mutableStateOf(false)
    }

    if (barState) {
        Column(Modifier.background(MaterialTheme.colors.background)) {
            ExpandableCalendar(
                calendarExpanded,
                {calendarExpanded = !calendarExpanded},
                onDateSelected = { onDateSelected(it) },
                currentDate
            )

            // Нижняя панель навигации
            Surface(color = MaterialTheme.colors.surface) {
                Surface(
                    color = MaterialTheme.colors.primaryVariant,
                    contentColor = contentColorFor(MaterialTheme.colors.primaryVariant),
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .selectableGroup(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // Food page
                        BottomNavigationItem(
                            selected = currentScreen == Screen.Food,
                            icon = {
                                if (currentScreen == Screen.Food)
                                    Icon(
                                        painterResource(id = R.drawable.food_icon),
                                        tint = AndroidGreen,
                                        contentDescription = stringResource(id = R.string.food_page)
                                    )
                                else
                                    Icon(
                                        painterResource(id = R.drawable.food_icon),
                                        contentDescription = null
                                    )
                            },
                            label = {
                                if (currentScreen == Screen.Food)
                                    Text(text = stringResource(id = R.string.food_page), color = AndroidGreen)
                                else
                                    Text(text = stringResource(id = R.string.food_page))
                            },
                            onClick = {
                                calendarExpanded = false
                                navController.navigate(Screen.Food.name)
                            }
                        )

                        // Profile page
                        BottomNavigationItem(
                            selected = currentScreen == Screen.Profile,
                            icon = {
                                if (currentScreen == Screen.Profile)
                                    Icon(
                                        painterResource(id = R.drawable.profile_icon),
                                        tint = AndroidGreen,
                                        contentDescription = stringResource(id = R.string.profile_page)
                                    )
                                else
                                    Icon(
                                        painterResource(id = R.drawable.profile_icon),
                                        contentDescription = stringResource(id = R.string.profile_page)
                                    )
                            },
                            label = {
                                if (currentScreen == Screen.Profile)
                                    Text(text = stringResource(id = R.string.profile_page), color = AndroidGreen)
                                else
                                    Text(text = stringResource(id = R.string.profile_page))
                            },
                            onClick = {
                                calendarExpanded = false
                                navController.navigate(Screen.Profile.name)
                            }
                        )

                        // Workout page
                        BottomNavigationItem(
                            selected = currentScreen == Screen.Workout,
                            icon = {
                                if (currentScreen == Screen.Workout)
                                    Icon(
                                        painterResource(id = R.drawable.workout_icon),
                                        tint = AndroidGreen,
                                        contentDescription = stringResource(id = R.string.workout_page)
                                    )
                                else
                                    Icon(
                                        painterResource(id = R.drawable.workout_icon),
                                        contentDescription = stringResource(id = R.string.workout_page)
                                    )
                            },
                            label = {
                                if (currentScreen == Screen.Workout)
                                    Text(text = stringResource(id = R.string.workout_page), color = AndroidGreen)
                                else
                                    Text(text = stringResource(id = R.string.workout_page))
                            },
                            onClick = {
                                calendarExpanded = false
                                navController.navigate(Screen.Workout.name)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseTopBar(
    label: String,
    @DrawableRes categoryIcon: Int,
    navigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Row(modifier = modifier
        .padding(start = 10.dp, end = 10.dp, top = 30.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = navigateBack
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                backgroundColor = BrightGray,
                elevation = 20.dp,
                modifier = Modifier
                    .size(54.dp)
            ) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = "Вернуться",
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Icon(painterResource(R.drawable.line),
            null,
            Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 4.dp)
                .weight(1f),
            Arsenic)

        Text(
            text = label,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Default,
            softWrap = false
        )

        Icon(painterResource(R.drawable.line),
            null,
            Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 4.dp)
                .weight(1f),
            Arsenic)

        Card(
            shape = RoundedCornerShape(20.dp),
            backgroundColor = BrightGray,
            elevation = 20.dp,
            modifier = Modifier
                .size(54.dp)
        ) {
            Icon(
                painterResource(id = categoryIcon),
                contentDescription = "Вернуться",
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun WorkoutEditTopBar(
    state: MutableState<TextFieldValue>,
    navigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
){
   Row(modifier = modifier
           .padding(start = 10.dp, end = 10.dp, top = 30.dp, bottom = 10.dp),
       verticalAlignment = Alignment.CenterVertically,
   ) {
       IconButton(
           onClick = navigateBack
       ) {
           Card(
               shape = RoundedCornerShape(20.dp),
               backgroundColor = BrightGray,
               elevation = 20.dp,
               modifier = Modifier.size(54.dp)
           ) {
               Icon(
                   Icons.Rounded.ArrowBack,
                   contentDescription = "Вернуться",
                   modifier = Modifier.padding(12.dp)
               )
           }
       }

       Spacer(modifier = Modifier.width(10.dp))

       SearchView(
           state = state,
           Modifier
               .weight(1f)
       )
   }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun Barr(){
    TrackItTheme {
        ExerciseTopBar(label = "Кардио", R.drawable.workout_icon)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchView(state: MutableState<TextFieldValue>, modifier: Modifier = Modifier){
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
            }
            .focusRequester(focusRequester)
            .height(54.dp),
        textStyle = MaterialTheme.typography.body1,
        placeholder = { Text(text = "Искать", color = Color.White, style = MaterialTheme.typography.body1) },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            focusManager.clearFocus()
        }),
        shape = RoundedCornerShape(10.dp), // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = AndroidGreen,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = MaterialTheme.colors.primaryVariant,
            focusedIndicatorColor = MaterialTheme.colors.surface,
            unfocusedIndicatorColor = MaterialTheme.colors.surface,
            disabledIndicatorColor = MaterialTheme.colors.surface
        )
    )
}