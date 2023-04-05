package com.example.trackit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trackit.calendar.ExpandableCalendar
import com.example.trackit.data.Screen
import com.example.trackit.ui.WorkoutPage
import com.example.trackit.ui.FoodPage
import com.example.trackit.ui.ProfilePage
import com.example.trackit.ui.theme.TrackItTheme
import java.time.LocalDate

@Composable
fun TrackItApp(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Current selected date in calendar
    val selectedDate = remember {
        mutableStateOf(LocalDate.now())
    }

    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Profile.name
    )

    Scaffold(
        bottomBar = {
            Column {
                ExpandableCalendar(
                    onDateSelected = {date -> selectedDate.value = date},
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
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Profile.name, Modifier.padding(innerPadding)) {
            composable(route = Screen.Profile.name){
                ProfilePage()
            }

            composable(route = Screen.Workout.name){
                WorkoutPage()
            }

            composable(route = Screen.Food.name){
                FoodPage()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTrackItApp(){
    TrackItTheme {
        TrackItApp()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTrackItAppDarkTheme(){
    TrackItTheme(darkTheme = true) {
        TrackItApp()
    }
}