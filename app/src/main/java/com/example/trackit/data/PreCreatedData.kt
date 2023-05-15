package com.example.trackit.data

import com.example.trackit.R
import com.example.trackit.data.workout.CardioExercise
import com.example.trackit.data.workout.StrengthExercise
import com.example.trackit.data.workout.WorkoutCategory
import java.time.Duration

val preCreatedCategoryList = listOf(
    WorkoutCategory(0, "Грудь", mutableListOf(
        StrengthExercise("Жим штанги лежа", 0, 0, 0),
        StrengthExercise("Жим гантелей лежа", 0, 0, 0),
        StrengthExercise("Жим гантелей на наклонной скамье", 0, 0, 0),
        StrengthExercise("Разводка гантелей на горизонтальной скамье", 0, 0, 0),
        StrengthExercise("Отжимания от пола", 0, 0, 0)
    ), R.drawable.chest),
    WorkoutCategory(1, "Руки", mutableListOf(
        StrengthExercise("Жим штанги на бицепс", 0, 0, 0),
        StrengthExercise("Сгибания рук со гантелями", 0, 0, 0),
        StrengthExercise("Отжимания на брусьях", 0, 0, 0),
        StrengthExercise("Молотковый подъем", 0, 0, 0),
        StrengthExercise("Упражнение \"Бритва\"", 0, 0, 0)
    ), R.drawable.arms),
    WorkoutCategory(2, "Плечи", mutableListOf(
        StrengthExercise("Жим гантелей стоя", 0, 0, 0),
        StrengthExercise("Французский жим", 0, 0, 0),
        StrengthExercise("Подтягивания к подбородку", 0, 0, 0),
        StrengthExercise("Вращения гантелей наружу и внутрь", 0, 0, 0),
        StrengthExercise("Жим штанги узким хватом", 0, 0, 0)
    ), R.drawable.shoulders),
    WorkoutCategory(3, "Ноги", mutableListOf(
        StrengthExercise("Приседания", 0, 0, 0),
        StrengthExercise("Выпады", 0, 0, 0),
        StrengthExercise("Подъем на носки с гантелями", 0, 0, 0),
        StrengthExercise("Жим ногами на тренажере", 0, 0, 0),
        StrengthExercise("Шаги с гантелями", 0, 0, 0)
    ), R.drawable.legs),
    WorkoutCategory(4,"Спина", mutableListOf(
        StrengthExercise("Подтягивания", 0, 0, 0),
        StrengthExercise("Широкое разведение рук с гантелями", 0, 0, 0),
        StrengthExercise("Тяга к груди", 0, 0, 0),
        StrengthExercise("Гиперэкстензия", 0, 0, 0),
        StrengthExercise("Отведение рук с резиной", 0, 0, 0)
    ), R.drawable.back),
    WorkoutCategory(5,"Растяжка", mutableListOf(
        CardioExercise("Дочка", Duration.ZERO),
        CardioExercise("Кобра", Duration.ZERO),
        CardioExercise("Глубокий выпад вперед", Duration.ZERO),
        CardioExercise("Поза мостика", Duration.ZERO),
        CardioExercise("Нисходящий собака", Duration.ZERO)
    ), R.drawable.stretching),
    WorkoutCategory(6,"Пресс", mutableListOf(
        StrengthExercise("Классические скручивания на пресс", 0, 0, 0),
        StrengthExercise("Боковые скручивания", 0, 0, 0),
        CardioExercise("Велосипед", Duration.ZERO),
        CardioExercise("Планка", Duration.ZERO),
        CardioExercise("Упражнение \"Ножницы\"", Duration.ZERO)
    ), R.drawable.abdominal),
    WorkoutCategory(7,"Кардио", mutableListOf(
        CardioExercise("Плавание", Duration.ZERO),
        StrengthExercise("Бёрпи", 0, 0, 0),
        CardioExercise("Прыжки на скакалке", Duration.ZERO),
        CardioExercise("Велотренажер", Duration.ZERO),
        CardioExercise("Бег", Duration.ZERO),
    ), R.drawable.cardio)
)