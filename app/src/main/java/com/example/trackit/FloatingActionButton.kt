package com.example.trackit

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.trackit.data.Screen

@Composable
fun FloatingButton(
    navController: NavController
){
    val currentRoute = navController.currentDestination?.route ?: Screen.Profile.name
    val currentIcon = when(currentRoute){
        Screen.Profile.name -> { Icons.Rounded.Edit }
        else -> { Icons.Rounded.Add }
    }

    FloatingActionButton(onClick = { /*TODO*/ }) {
        Icon(currentIcon, contentDescription = null)
    }
}