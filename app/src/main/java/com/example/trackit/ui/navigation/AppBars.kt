package com.example.trackit.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackit.R
import com.example.trackit.calendar.ExpandableCalendar
import com.example.trackit.data.Screen
import java.time.LocalDate
import androidx.compose.material.TopAppBar

@Composable
fun BottomBar(
    barState: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    navController: NavController,
    currentScreen: Screen
){
    if (barState) {
        Column {
            ExpandableCalendar(
                onDateSelected = { onDateSelected(it) },
                Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            )
            BottomNavigation {
                // Food page
                BottomNavigationItem(
                    selected = currentScreen == Screen.Food,
                    icon = { Icon(Icons.Rounded.Favorite, contentDescription = null) },
                    label = { Text(text = stringResource(id = R.string.food_page)) },
                    onClick = {
                        navController.navigate(Screen.Food.name)
                    }
                )

                // Profile page
                BottomNavigationItem(
                    selected = currentScreen == Screen.Profile,
                    icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    label = { Text(text = stringResource(id = R.string.profile_page)) },
                    onClick = {
                        navController.navigate(Screen.Profile.name)
                    }
                )

                // Fitness page
                BottomNavigationItem(
                    selected = currentScreen == Screen.Workout,
                    icon = { Icon(Icons.Rounded.Favorite, contentDescription = null) },
                    label = { Text(text = stringResource(id = R.string.workout_page)) },
                    onClick = {
                        navController.navigate(Screen.Workout.name)
                    }
                )
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