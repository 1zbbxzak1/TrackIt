package com.example.trackit.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.trackit.ui.theme.TrackItTheme

@Composable
fun FitnessPage(
    modifier: Modifier = Modifier
){
    Text(text = "Fitness", fontSize = 50.sp)
}

@Preview(showBackground = true)
@Composable
fun PreviewFitnessPage(){
    TrackItTheme {
        FitnessPage()
    }
}