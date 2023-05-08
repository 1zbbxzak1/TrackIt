package com.example.trackit

import android.annotation.SuppressLint
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
import com.example.trackit.ui.FoodPage
import com.example.trackit.ui.Nutrition.Food.FoodScreen
import com.example.trackit.ui.ProfilePage
import com.example.trackit.ui.navigation.BottomBar
import com.example.trackit.ui.theme.TrackItTheme
import com.example.trackit.ui.workout.WorkoutPage
import com.example.trackit.ui.workout.category.WorkoutCategoryScreen
import com.example.trackit.ui.workout.exercise.WorkoutExerciseScreen
import java.time.LocalDate

@SuppressLint("UnrememberedMutableState")
@Composable
fun TrackItApp(
    modifier: Modifier = Modifier,
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
        Screen.WorkoutCategory.name -> {
            bottomBarState.value = false
            topBarState.value = true
            floatingButtonState.value = false
        }
        Screen.NutritionFood.name -> {
            bottomBarState.value = false
            topBarState.value = true
            floatingButtonState.value = false
        }
    }

    Scaffold(
        //floatingActionButton = {FloatingButton(onClick = {})},
        //isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomBar(
                selectedDate.value,
                bottomBarState.value,
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
                WorkoutPage(navigateToEntry = {navController.navigate(Screen.WorkoutCategory.name)}, selectedDate.value)
            }

            composable(route = Screen.WorkoutCategory.name){
                WorkoutCategoryScreen(
                    onCategorySelect = {categoryId ->
                        navController.currentBackStackEntry?.arguments?.putInt("categoryId", categoryId)
                        navController.navigate(Screen.WorkoutExercise.name)

                    },
                    navigateBack = {navController.popBackStack()}
                )
            }

            composable(
                route = Screen.WorkoutExercise.name
            ){
                val categoryId = navController.previousBackStackEntry?.arguments?.getInt("categoryId", 0)
                WorkoutExerciseScreen(
                    categoryId,
                    navigateBack = {navController.popBackStack()},
                    navigateToWorkoutPage = {navController.navigate(Screen.Workout.name)},
                    selectedDate = selectedDate.value
                )}

            composable(route = Screen.Food.name){
                FoodPage(
                    navigateToEntry = {navController.navigate(Screen.NutritionFood.name)},
                    selectedDate = selectedDate.value
                )
            }

            composable(route = Screen.NutritionFood.name){
                FoodScreen(
                    navigateBack = { navController.popBackStack() },
                    navigateToFoodPage = { navController.navigate(Screen.Food.name) }
                )
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