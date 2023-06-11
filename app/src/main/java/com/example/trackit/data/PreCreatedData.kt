package com.example.trackit.data

import com.example.trackit.R
import com.example.trackit.data.workout.CardioExercise
import com.example.trackit.data.workout.StrengthExercise
import com.example.trackit.data.workout.WorkoutCategory
import java.time.Duration

val preCreatedCategoryList = listOf(
    WorkoutCategory(0, "Грудь", mutableListOf(
        StrengthExercise(0, "Жим штанги лежа", 0, 0, 0),
        StrengthExercise(1, "Жим гантелей лежа", 0, 0, 0),
        StrengthExercise(2, "Жим гантелей на наклонной скамье", 0, 0, 0),
        StrengthExercise(3, "Разводка гантелей на горизонтальной скамье", 0, 0, 0),
        StrengthExercise(4, "Отжимания от пола", 0, 0, 0)
    ), R.drawable.chest),
    WorkoutCategory(1, "Руки", mutableListOf(
        StrengthExercise(0, "Жим штанги на бицепс", 0, 0, 0),
        StrengthExercise(1, "Сгибания рук со гантелями", 0, 0, 0),
        StrengthExercise(2, "Отжимания на брусьях", 0, 0, 0),
        StrengthExercise(3, "Молотковый подъем", 0, 0, 0),
        StrengthExercise(4, "Упражнение \"Бритва\"", 0, 0, 0)
    ), R.drawable.arms),
    WorkoutCategory(2, "Плечи", mutableListOf(
        StrengthExercise(0, "Жим гантелей стоя", 0, 0, 0),
        StrengthExercise(1, "Французский жим", 0, 0, 0),
        StrengthExercise(2, "Подтягивания к подбородку", 0, 0, 0),
        StrengthExercise(3, "Вращения гантелей наружу и внутрь", 0, 0, 0),
        StrengthExercise(4, "Жим штанги узким хватом", 0, 0, 0)
    ), R.drawable.shoulders),
    WorkoutCategory(3, "Ноги", mutableListOf(
        StrengthExercise(0, "Приседания", 0, 0, 0),
        StrengthExercise(1, "Выпады", 0, 0, 0),
        StrengthExercise(2, "Подъем на носки с гантелями", 0, 0, 0),
        StrengthExercise(3, "Жим ногами на тренажере", 0, 0, 0),
        StrengthExercise(4, "Шаги с гантелями", 0, 0, 0)
    ), R.drawable.legs),
    WorkoutCategory(4,"Спина", mutableListOf(
        StrengthExercise(0, "Подтягивания", 0, 0, 0),
        StrengthExercise(1, "Широкое разведение рук с гантелями", 0, 0, 0),
        StrengthExercise(2, "Тяга к груди", 0, 0, 0),
        StrengthExercise(3, "Гиперэкстензия", 0, 0, 0),
        StrengthExercise(4, "Отведение рук с резиной", 0, 0, 0)
    ), R.drawable.back),
    WorkoutCategory(5,"Растяжка", mutableListOf(
        CardioExercise(0, "Дочка", Duration.ZERO),
        CardioExercise(1, "Кобра", Duration.ZERO),
        CardioExercise(2, "Глубокий выпад вперед", Duration.ZERO),
        CardioExercise(3, "Поза мостика", Duration.ZERO),
        CardioExercise(4, "Нисходящий собака", Duration.ZERO)
    ), R.drawable.stretching),
    WorkoutCategory(6,"Пресс", mutableListOf(
        StrengthExercise(0, "Классические скручивания на пресс", 0, 0, 0),
        StrengthExercise(1, "Боковые скручивания", 0, 0, 0),
        CardioExercise(2, "Велосипед", Duration.ZERO),
        CardioExercise(3, "Планка", Duration.ZERO),
        CardioExercise(4, "Упражнение \"Ножницы\"", Duration.ZERO)
    ), R.drawable.abdominal),
    WorkoutCategory(7,"Кардио", mutableListOf(
        CardioExercise(0, "Плавание", Duration.ZERO),
        StrengthExercise(1, "Бёрпи", 0, 0, 0),
        CardioExercise(2, "Прыжки на скакалке", Duration.ZERO),
        CardioExercise(3, "Велотренажер", Duration.ZERO),
        CardioExercise(4, "Бег", Duration.ZERO),
    ), R.drawable.cardio)
)