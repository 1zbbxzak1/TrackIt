package com.example.trackit.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.trackit.data.Screen
import com.example.trackit.ui.theme.TrackItTheme

@Composable
fun WorkoutPage(
    modifier: Modifier = Modifier
){
    Text(text = Screen.Workout.name, fontSize = 50.sp)
}

@Preview(showBackground = true)
@Composable
fun PreviewFitnessPage(){
    TrackItTheme {
        WorkoutPage()
    }
}