package com.example.trackit.data.workout

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.trackit.R
import java.time.Duration
import java.time.LocalDate

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

data class CategoryCount(
    val category: String,
    val count: Int
)

data class ExerciseCount(
    val exercise: String,
    val count: Int
)

sealed class Exercise {
    abstract var id: Int
    abstract val name: String
}

data class CardioExercise(
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0,
    override val name: String,
    val time: Duration
) : Exercise()

data class StrengthExercise(
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0,
    override val name: String,
    val weight: Int,
    val repeatCount: Int,
    val approachCount: Int
) : Exercise()

@Entity(tableName = "workout_categories")
data class WorkoutCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val exercises: MutableList<Exercise> = mutableListOf(),
    val icon: Int = R.drawable.workout_icon
)


class ExerciseConverter {
    @TypeConverter
    fun fromExercise(exercise: Exercise): String {
        return when (exercise) {
            is CardioExercise -> "cardio:${exercise.id}:${exercise.name}:${exercise.time.toString()}"
            is StrengthExercise -> "strength:${exercise.id}:${exercise.name}:${exercise.weight}:${exercise.repeatCount}:${exercise.approachCount}"
        }
    }

    @TypeConverter
    fun toExercise(value: String): Exercise {
        val parts = value.split(":")
        return when (parts[0]) {
            "cardio" -> CardioExercise(
                parts[1].toInt(),
                parts[2],
                Duration.parse(parts[3])
            )
            "strength" -> StrengthExercise(
                parts[1].toInt(),
                parts[2],
                parts[3].toInt(),
                parts[4].toInt(),
                parts[5].toInt()
            )
            else -> CardioExercise(0, "", Duration.ZERO)
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
        return "${category.name}:${category.id}:${category.icon}"
    }

    @TypeConverter
    fun toWorkoutCategory(value: String): WorkoutCategory {
        val parts = value.split(":")
        val categoryName = parts[0]
        val categoryId = parts[1].toInt()
        val categoryIcon = parts[2].toInt()
        return WorkoutCategory(categoryId, categoryName, icon = categoryIcon)
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