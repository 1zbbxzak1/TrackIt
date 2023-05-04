package com.example.trackit.ui.theme

import android.graphics.Color.parseColor
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Arsenic,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Arsenic,
    secondary = Teal200,
    surface = BrightGray,
    onError = PermanentGeraniumLake,
    onSurface = Arsenic,
    background = Color.White

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun TrackItTheme(
    darkTheme: Boolean = false, //isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    val colors: Colors

    if (darkTheme){
        colors = DarkColorPalette
        systemUiController.setSystemBarsColor(
            color = colors.background
        )
        systemUiController.setNavigationBarColor(
            color = colors.primaryVariant
        )
    }
    else {
        colors = LightColorPalette
        systemUiController.setSystemBarsColor(
            color = colors.background
        )
        systemUiController.setStatusBarColor(
            color = colors.primaryVariant
        )
        systemUiController.setNavigationBarColor(
            color = colors.primaryVariant
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}