package com.example.trackit.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trackit.R
import com.example.trackit.calendar.ExpandableCalendar
import com.example.trackit.data.Screen
import com.example.trackit.ui.theme.*
import java.time.LocalDate

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

    AnimatedVisibility(
        visible = barState,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing),
        ),
        exit = ExitTransition.None,

    ) {
        Column(
            modifier = Modifier
        ) {
            ExpandableCalendar(
                calendarExpanded,
                { calendarExpanded = !calendarExpanded },
                onDateSelected = { onDateSelected(it) },
                currentDate,
                modifier = Modifier.background(Color.Transparent)
            )

            // Нижняя панель навигации
            Surface(
                color = MaterialTheme.colors.primaryVariant,
                contentColor = contentColorFor(MaterialTheme.colors.primaryVariant),
                //shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp)
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
                                Text(
                                    text = stringResource(id = R.string.food_page),
                                    color = AndroidGreen
                                )
                            else
                                Text(text = stringResource(id = R.string.food_page))
                        },
                        onClick = {
                            calendarExpanded = false
                            if (currentScreen != Screen.Food) navController.navigate(Screen.Food.name)
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
                                Text(
                                    text = stringResource(id = R.string.profile_page),
                                    color = AndroidGreen
                                )
                            else
                                Text(text = stringResource(id = R.string.profile_page))
                        },
                        onClick = {
                            calendarExpanded = false
                            if (currentScreen != Screen.Profile) navController.navigate(Screen.Profile.name)
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
                                Text(
                                    text = stringResource(id = R.string.workout_page),
                                    color = AndroidGreen
                                )
                            else
                                Text(text = stringResource(id = R.string.workout_page))
                        },
                        onClick = {
                            calendarExpanded = false
                            if (currentScreen != Screen.Workout) navController.navigate(Screen.Workout.name)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TopBarWithLabel(
    label: String,
    @DrawableRes icon: Int,
    iconColor: Color = AndroidGreen,
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
                elevation = 8.dp,
                modifier = Modifier
                    .size(54.dp)
            ) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = "Вернуться",
                    modifier = Modifier
                        .size(40.dp)
                        .requiredSize(40.dp),
                    tint = Arsenic
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
            softWrap = false,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Arsenic
            )
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
            elevation = 8.dp,
            modifier = Modifier
                .size(54.dp)
        ) {
            Icon(
                painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
                    .size(40.dp)
                    .requiredSize(40.dp),
                tint = iconColor
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
               elevation = 8.dp,
               modifier = Modifier.size(54.dp)
           ) {
               Icon(
                   Icons.Rounded.ArrowBack,
                   contentDescription = "Вернуться",
                   modifier = Modifier
                       .size(40.dp)
                       .requiredSize(40.dp),
                   tint = Arsenic
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchView(state: MutableState<TextFieldValue>, modifier: Modifier = Modifier){
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
            }
            .focusRequester(focusRequester)
            .height(54.dp),
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        textStyle = TextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 24.sp,
            color = CaptionColor
        ),
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            focusManager.clearFocus()
        })
    ) {innerTextField ->
        Card(
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Arsenic,
            elevation = 6.dp,
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.search), tint = CaptionColor,
                    contentDescription = null, modifier = Modifier
                        .size(18.dp)
                        .requiredSize(18.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.value.text.isEmpty()) {
                        Text(
                            text = "Искать",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Light,
                            color = CaptionColor
                        )
                    }
                    innerTextField()
                }
            }
        }
    }
}