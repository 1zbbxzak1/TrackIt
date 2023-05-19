package com.example.trackit.ui.navigation

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.trackit.data.Screen
import com.example.trackit.ui.theme.AndroidGreen

@Composable
fun FloatingButton(
    currentRoute: String,
    onClick: () -> Unit = {}
){
    val currentIcon = when(currentRoute){
        Screen.Profile.name -> { Icons.Rounded.Edit }
        else -> { Icons.Rounded.Add }
    }

    FloatingActionButton(
        onClick = { onClick() },
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = Modifier.size(50.dp)
    ) {
        Icon(
            currentIcon, contentDescription = null,
            tint = AndroidGreen,
            modifier = Modifier
                .size(40.dp)
                .requiredSize(40.dp)
        )
    }
}