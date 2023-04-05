package com.example.trackit

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val topBarState = rememberSaveable { (mutableStateOf(false)) }
    val floatingButtonState = rememberSaveable{ (mutableStateOf(true)) }

    when (backStackEntry?.destination?.route){
        Screen.Profile.name -> {
            bottomBarState.value = true
            topBarState.value = false
            floatingButtonState.value = false
        }
        Screen.Food.name -> {
            bottomBarState.value = true
            topBarState.value = false
            floatingButtonState.value = true
        }
        Screen.Workout.name -> {
            bottomBarState.value = true
            topBarState.value = false
            floatingButtonState.value = true
        }
    }

    Scaffold(
        floatingActionButton = {FloatingButton(navController)},
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomBar(
                onDateSelected = {selectedDate.value = it},
                navController = navController,
                currentScreen = currentScreen
            )
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