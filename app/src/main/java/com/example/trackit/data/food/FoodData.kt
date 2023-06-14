package com.example.trackit.ui.Nutrition

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.*

data class FoodData(
    var id: Long = 0,
    val name: String,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val calories: Double,
    val gramsEntered: Int
)

data class FoodCount(
    val food: FoodData,
    val count: Int
)

sealed class Meal {
    abstract val id: Long
    abstract val date: LocalDate
    abstract val food: FoodData
}

@Entity(tableName = "breakfast")
data class Breakfast(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = generateUniqueID(),
    override val date: LocalDate,
    val foodBreakfast: FoodData
) : Meal() {
    override val food: FoodData
        get() = foodBreakfast
}

@Entity(tableName = "lunch")
data class Lunch(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = generateUniqueID(),
    override val date: LocalDate,
    val foodLunch: FoodData
) : Meal() {
    override val food: FoodData
        get() = foodLunch
}
@Entity(tableName = "dinner")
data class Dinner(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = generateUniqueID(),
    override val date: LocalDate,
    val foodDinner: FoodData
) : Meal() {
    override val food: FoodData
        get() = foodDinner
}
@Entity(tableName = "snack")
data class Snack(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = generateUniqueID(),
    override val date: LocalDate,
    val foodSnack: FoodData
) : Meal() {
    override val food: FoodData
        get() = foodSnack
}

@Entity(tableName = "total")
data class Total(
    @PrimaryKey
    val date: LocalDate,
    val proteins: Int = 0,
    val fats: Int = 0,
    val carbs: Int  = 0,
    val calories: Int  = 0
)

fun generateUniqueID(): Long {
    return UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
}