package com.example.trackit.ui

import android.view.LayoutInflater
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.trackit.R
import com.example.trackit.ui.theme.TrackItTheme


@Composable
fun ProfilePage(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {context ->
            LayoutInflater.from(context).inflate(R.layout.activity_profile, null)
        }
    ) //пока только так, позже разберусь с ProfileActivity
}

@Preview(showBackground = true)
@Composable
fun PreviewHomePage(){
    TrackItTheme {
        ProfilePage()
    }
}