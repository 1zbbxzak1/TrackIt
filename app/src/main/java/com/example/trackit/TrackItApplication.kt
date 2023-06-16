package com.example.trackit

import android.app.Application
import com.example.trackit.data.AppContainer
import com.example.trackit.data.AppDataContainer

class TrackItApplication: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}