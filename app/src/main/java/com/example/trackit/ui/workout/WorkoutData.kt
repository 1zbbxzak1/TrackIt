package com.example.trackit.ui.workout

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import kotlin.time.toKotlinDuration

@Entity(tableName = "workout_items")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val exercise: Exercise,
    val category: WorkoutCategory,
    val date: LocalDate,
    var completed: Boolean
)

sealed class Exercise {
    abstract val name: String
}

data class CardioExercise(
    override val name: String,
    val time: Duration
) : Exercise()

data class StrengthExercise(
    override val name: String,
    val weight: Float,
    val repeatCount: Int,
    val approachCount: Int
) : Exercise()

@Entity(tableName = "workout_categories")
data class WorkoutCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val exercises: MutableList<Exercise> = mutableListOf()
)


class ExerciseConverter {
    @TypeConverter
    fun fromExercise(exercise: Exercise): String {
        return when (exercise) {
            is CardioExercise -> "cardio:${exercise.name}:${exercise.time.toString()}"
            is StrengthExercise -> "strength:${exercise.name}:${exercise.weight}:${exercise.repeatCount}:${exercise.approachCount}"
        }
    }

    @TypeConverter
    fun toExercise(value: String): Exercise {
        val parts = value.split(":")
        return when (parts[0]) {
            "cardio" -> CardioExercise(parts[1], Duration.parse(parts[2]))
            "strength" -> StrengthExercise(parts[1], parts[2].toFloat(), parts[3].toInt(), parts[4].toInt())
            else -> CardioExercise("", Duration.ZERO)
        }
    }

    @TypeConverter
    fun fromString(value: String?): List<Exercise> {
        return value?.let {
            it.split(",").map { toExercise(it) }
        } ?: emptyList()
    }

    @TypeConverter
    fun toString(exercises: List<Exercise>): String {
        return exercises.joinToString(separator = ",") { fromExercise(it) }
    }

    @TypeConverter
    fun fromWorkoutCategory(category: WorkoutCategory): String {
        return "${category.name}:${category.id}"
    }

    @TypeConverter
    fun toWorkoutCategory(value: String): WorkoutCategory {
        val parts = value.split(":")
        val categoryName = parts[0]
        val categoryId = parts[1].toInt()
        return WorkoutCategory(categoryId, categoryName)
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