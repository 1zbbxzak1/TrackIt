package com.example.trackit

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trackit.data.Page
import com.example.trackit.ui.FitnessPage
import com.example.trackit.ui.FoodPage
import com.example.trackit.ui.HomePage
import com.example.trackit.ui.theme.TrackItTheme

@Composable
fun TrackItApp(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentPage = Page.valueOf(
        backStackEntry?.destination?.route ?: Page.Home.name
    )

    Scaffold(
        bottomBar = {
            BottomNavigation {
                // Food page
                BottomNavigationItem(
                    selected = currentPage == Page.Food,
                    icon = { Icon(Icons.Rounded.Favorite, contentDescription = null) },
                    label = { Text(text = Page.Food.name) },
                    onClick = {
                        navController.navigate(Page.Food.name)
                    }
                )

                // Home page
                BottomNavigationItem(
                    selected = currentPage == Page.Home,
                    icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    label = { Text(text = Page.Home.name) },
                    onClick = {
                        navController.navigate(Page.Home.name)
                    }
                )

                // Fitness page
                BottomNavigationItem(
                    selected = currentPage == Page.Fitness,
                    icon = { Icon(Icons.Rounded.Favorite, contentDescription = null) },
                    label = { Text(text = Page.Fitness.name) },
                    onClick = {
                        navController.navigate(Page.Fitness.name)
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Page.Home.name, Modifier.padding(innerPadding)) {
            composable(route = Page.Home.name){
                HomePage()
            }

            composable(route = Page.Fitness.name){
                FitnessPage()
            }

            composable(route = Page.Food.name){
                FoodPage()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTrackItApp(){
    TrackItTheme {
        TrackItApp()
    }
}