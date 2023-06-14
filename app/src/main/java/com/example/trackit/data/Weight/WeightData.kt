package com.example.trackit.data.Weight

import androidx.room.Entity
import androidx.room.PrimaryKey
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

data class DateTimeWrapper(val date: LocalDate, val time: LocalTime)