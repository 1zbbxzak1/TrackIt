package com.example.trackit.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.trackit.TrackItApp
import com.example.trackit.ui.theme.TrackItTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            TrackItTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // Получение данных из WelcomeScreen
                    val gender = intent.getStringExtra("gender")
                    val age = intent.getIntExtra("age", 0)
                    val height = intent.getIntExtra("height", 0)

                    TrackItApp(Modifier.systemBarsPadding(), gender, age, height)
                }
            }
        }
    }
}