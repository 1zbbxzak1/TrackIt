package com.example.trackit.ui.navigation

import android.content.res.Resources.Theme
import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackit.R
import com.example.trackit.calendar.ExpandableCalendar
import com.example.trackit.data.Screen
import java.time.LocalDate
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.trackit.ui.theme.AndroidGreen

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
fun WorkoutEditTopBar(
    title: String,
    navigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
){
        TopAppBar(
            title = { Text(text = title)},
            modifier = modifier,
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
}