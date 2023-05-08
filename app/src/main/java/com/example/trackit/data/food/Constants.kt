package com.example.trackit.data.food

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object Globals {
    var TotalProteins by mutableStateOf(0)
    var TotalFats by mutableStateOf(0)
    var TotalCarbs by mutableStateOf(0)
    var TotalCalories by mutableStateOf(0)
}