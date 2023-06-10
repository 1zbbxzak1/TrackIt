package com.example.trackit.data.Weight

import android.widget.TimePicker
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "weight_entries")
data class WeightEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val time: LocalTime,
    val date: LocalDate,
    val weight: Double
)

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

