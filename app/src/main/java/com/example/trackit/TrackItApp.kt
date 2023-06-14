package com.example.trackit

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.trackit.data.Screen
import com.example.trackit.ui.FoodPage
import com.example.trackit.ui.Nutrition.Food.FoodScreen
import com.example.trackit.ui.ProfilePage
import com.example.trackit.ui.navigation.BottomBar
import com.example.trackit.ui.statistics.StatisticsPage
import com.example.trackit.ui.workout.WorkoutPage
import com.example.trackit.ui.workout.category.WorkoutCategoryScreen
import com.example.trackit.ui.workout.exercise.WorkoutExerciseScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun TrackItApp(
    modifier: Modifier = Modifier,
    gender: String?,
    age: Int,
    height: Int
){
    val navController = rememberAnimatedNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Current selected date in calendar
    val selectedDate = remember {
        mutableStateOf(LocalDate.now())
    }

    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Profile.name
    )

    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    bottomBarState.value = when (backStackEntry?.destination?.route){
        Screen.Profile.name, Screen.Food.name, Screen.Workout.name -> true
        else -> false
    }

    Column(modifier = modifier.fillMaxSize()) {
        AnimatedNavHost(
            navController,
            startDestination = Screen.Profile.name,
            modifier = Modifier.weight(100f)
        ) {
            composable(
                route = Screen.Profile.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        Screen.Food.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(200)
                        )
                        Screen.Workout.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(200)
                        )
                        Screen.Statistics.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(200)
                        )
                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Screen.Food.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(200)
                        )
                        Screen.Workout.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(200)
                        )
                        Screen.Statistics.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(200)
                        )
                        else -> null
                    }
                }
            ) {
                ProfilePage(
                    selectedDate.value,
                    time = LocalTime.now(),
                    navigateToStats = { navController.navigate(Screen.Statistics.name) },
                    gender,
                    age,
                    height
                )
            }

            composable(
                route = Screen.Workout.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        Screen.WorkoutExercise.name -> fadeIn()
                        Screen.WorkoutCategory.name -> fadeIn()
                        else -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(200)
                        )
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Screen.WorkoutCategory.name -> ExitTransition.None
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(200)
                        )
                    }
                },
                popEnterTransition = {
                    fadeIn()
                },
                popExitTransition = {
                    ExitTransition.None
                }
            ) {
                WorkoutPage(
                    navigateToEntry = {
                        navController.navigate(Screen.WorkoutCategory.name)
                    },
                    selectedDate.value,
                )
            }

            composable(
                route = Screen.WorkoutCategory.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(400)
                    )
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Screen.Workout.name -> ExitTransition.None
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(400)
                        )
                    }
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(400)
                    )
                },
                popExitTransition = {
                    ExitTransition.None
                }
            ) {
                WorkoutCategoryScreen(
                    onCategorySelect = { categoryId, searchedText ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("categoryId", categoryId)
                        navController.currentBackStackEntry?.savedStateHandle?.set("searchedText", searchedText)
                        navController.navigate(Screen.WorkoutExercise.name)
                    },
                    navigateBack = { navController.popBackStack() },
                    navigateToWorkoutPage = { navController.navigate(Screen.Workout.name) },
                    selectedDate = selectedDate.value
                )
            }

            composable(
                route = Screen.WorkoutExercise.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(400)
                    )
                },
                exitTransition = {
                    ExitTransition.None
                },
                popEnterTransition = {
                    EnterTransition.None
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(400)
                    )
                }
            ) {
                val categoryId = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("categoryId")
                val searchedText = navController.previousBackStackEntry?.savedStateHandle?.get<String>("searchedText") ?: ""
                WorkoutExerciseScreen(
                    categoryId = categoryId,
                    navigateBack = { navController.popBackStack() },
                    navigateToWorkoutPage = { navController.navigate(Screen.Workout.name) },
                    searchedText = searchedText,
                    selectedDate = selectedDate.value
                )
            }

            composable(
                route = Screen.Food.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        Screen.NutritionFood.name -> fadeIn()
                        else -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            tween(200)
                        )
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Screen.NutritionFood.name -> ExitTransition.None
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            tween(200)
                        )
                    }
                },
                popEnterTransition = {
                    fadeIn()
                },
            ) {
                FoodPage(
                    navigateToFoodScreen = { navController.navigate(Screen.NutritionFood.name) },
                    selectedDate = selectedDate.value
                )
            }

            composable(
                route = Screen.NutritionFood.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(400)
                    )
                },
                exitTransition = {
                    ExitTransition.None
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(400)
                    )
                },
                popExitTransition = {
                    ExitTransition.None
                }
            ) {
                FoodScreen(
                    navigateBack = { navController.popBackStack() },
                    navigateToFoodPage = { navController.navigate(Screen.Food.name) },
                    selectedDate = selectedDate.value
                )
            }

            composable(
                route = Screen.Statistics.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(400)
                    )
                },
                exitTransition = {
                    ExitTransition.None
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(400)
                    )
                },
                popExitTransition = {
                    ExitTransition.None
                }
            ) {
                StatisticsPage(
                    navigateBack = { navController.popBackStack() }
                )
            }
        }

        Spacer(Modifier.weight(1f))

        BottomBar(
            selectedDate.value,
            bottomBarState.value,
            onDateSelected = { selectedDate.value = it },
            navController = navController,
            currentScreen = currentScreen
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewTrackItApp(){
//    TrackItTheme {
//        TrackItApp()
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewTrackItAppDarkTheme(){
//    TrackItTheme(darkTheme = true) {
//        TrackItApp()
//    }
//}