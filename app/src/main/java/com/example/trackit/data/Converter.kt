package com.example.trackit.data

import androidx.room.TypeConverter
import com.example.trackit.ui.Nutrition.FoodData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalTime

class DateConverter {
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }
}

class LocalTimeConverter {
    @TypeConverter
    fun fromLocalTime(localTime: LocalTime?): String? {
        return localTime?.toString()
    }

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }
}

class FoodTypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromFoodData(foodData: FoodData): String {
        return gson.toJson(foodData)
    }

    @TypeConverter
    fun toFoodData(json: String): FoodData {
        val type = object : TypeToken<FoodData>() {}.type
        return gson.fromJson(json, type)
    }
}



