package com.example.trackit.data.workout

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.trackit.R
import com.example.trackit.data.food.generateUniqueID
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
    abstract var id: Long
    abstract val name: String
}

data class CardioExercise(
    override val name: String,
    val time: Duration,
    override var id: Long = generateUniqueID()
) : Exercise()

data class StrengthExercise(
    override val name: String,
    val weight: Int,
    val repeatCount: Int,
    val approachCount: Int,
    override var id: Long = generateUniqueID()
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
            is CardioExercise -> "cardio:${exercise.name}:${exercise.time.toString()}:${exercise.id}"
            is StrengthExercise -> "strength:${exercise.name}:${exercise.weight}:${exercise.repeatCount}:${exercise.approachCount}:${exercise.id}"
        }
    }

    @TypeConverter
    fun toExercise(value: String): Exercise {
        val parts = value.split(":")
        return when (parts[0]) {
            "cardio" -> {
                if (parts.size < 5){
                    Log.d("??????", parts.toString())
                }
                CardioExercise(
                    parts[1],
                    Duration.parse(parts[2]),
                    if (parts.size == 4) parts[3].toLong() else generateUniqueID(),
                )
            }
            "strength" -> {
                if (parts.size < 7) Log.d("??????", parts.toString())
                StrengthExercise(
                    parts[1],
                    parts[2].toInt(),
                    parts[3].toInt(),
                    parts[4].toInt(),
                    if (parts.size == 6) parts[5].toLong() else generateUniqueID(),
                )
            }
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