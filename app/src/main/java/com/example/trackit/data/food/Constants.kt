@file:Suppress("UNREACHABLE_CODE")

package com.example.trackit.data.food

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.trackit.ui.Nutrition.DailyLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.util.*

fun generateUniqueID(): Long {
    return UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
}

object ListFood {
    var logs = mutableListOf<DailyLog>()
}

fun getLogForDate(date: LocalDate): DailyLog {
    val existingLog = ListFood.logs.find { it.date == date }
    return existingLog ?: DailyLog(
        date = date,
        breakfastFoods = mutableListOf(),
        lunchFoods = mutableListOf(),
        dinnerFoods = mutableListOf(),
        snackFoods = mutableListOf(),
        totalProteins = 0,
        totalFats = 0,
        totalCarbs = 0,
        totalCalories = 0
    )
}