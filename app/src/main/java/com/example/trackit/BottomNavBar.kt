package com.example.trackit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackit.calendar.ExpandableCalendar
import com.example.trackit.data.Screen
import java.time.LocalDate

@Composable
fun BottomBar(
    onDateSelected: (LocalDate) -> Unit,
    navController: NavController,
    currentScreen: Screen
){
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