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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trackit.calendar.ExpandableCalendar
import com.example.trackit.data.Screen
import com.example.trackit.ui.FitnessPage
import com.example.trackit.ui.FoodPage
import com.example.trackit.ui.HomePage
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
        backStackEntry?.destination?.route ?: Screen.Home.name
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
                        label = { Text(text = Screen.Food.name) },
                        onClick = {
                            navController.navigate(Screen.Food.name)
                        }
                    )

                    // Home page
                    BottomNavigationItem(
                        selected = currentScreen == Screen.Home,
                        icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        label = { Text(text = Screen.Home.name) },
                        onClick = {
                            navController.navigate(Screen.Home.name)
                        }
                    )

                    // Fitness page
                    BottomNavigationItem(
                        selected = currentScreen == Screen.Fitness,
                        icon = { Icon(Icons.Rounded.Favorite, contentDescription = null) },
                        label = { Text(text = Screen.Fitness.name) },
                        onClick = {
                            navController.navigate(Screen.Fitness.name)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.name, Modifier.padding(innerPadding)) {
            composable(route = Screen.Home.name){
                HomePage()
            }

            composable(route = Screen.Fitness.name){
                FitnessPage()
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