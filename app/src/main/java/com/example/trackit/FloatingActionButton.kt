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
    currentRoute: String,
    onClick: () -> Unit = {}
){
    val currentIcon = when(currentRoute){
        Screen.Profile.name -> { Icons.Rounded.Edit }
        else -> { Icons.Rounded.Add }
    }

    FloatingActionButton(onClick = { onClick() }) {
        Icon(currentIcon, contentDescription = null)
    }
}