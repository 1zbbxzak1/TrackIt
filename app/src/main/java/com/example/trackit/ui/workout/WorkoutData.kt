package com.example.trackit.ui.workout

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDate

@Entity(tableName = "workout_items")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val exercise: Exercise,
    val date: LocalDate
)

data class Exercise(
    val name: String
)

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val exercises: List<Exercise>
)

class ExerciseConverter {
    @TypeConverter
    fun fromExercise(exercise: Exercise): String {
        return exercise.name
    }

    @TypeConverter
    fun toExercise(name: String): Exercise {
        return Exercise(name)
    }
}

class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }
}