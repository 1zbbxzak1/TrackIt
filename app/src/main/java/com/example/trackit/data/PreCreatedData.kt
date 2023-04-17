package com.example.trackit.data

import com.example.trackit.ui.workout.Exercise
import com.example.trackit.ui.workout.WorkoutCategory

val preCreatedCategoryList = listOf(
    WorkoutCategory(0, "Грудные мышцы", mutableListOf(
        Exercise("Жим штанги лежа"),
        Exercise("Жим гантелей лежа"),
        Exercise("Жим гантелей на наклонной скамье"),
        Exercise("Разводка гантелей на горизонтальной скамье"),
        Exercise("Отжимания от пола")
    )
    ),
    WorkoutCategory(1, "Руки", mutableListOf(
        Exercise("Жим штанги на бицепс"),
        Exercise("Сгибания рук со гантелями"),
        Exercise("Отжимания на брусьях"),
        Exercise("Молотковый подъем"),
        Exercise("Упражнение \"Бритва\"")
    )),
    WorkoutCategory(2, "Плечи", mutableListOf(
        Exercise("Жим гантелей стоя"),
        Exercise("Французский жим"),
        Exercise("Подтягивания к подбородку"),
        Exercise("Вращения гантелей наружу и внутрь"),
        Exercise("Жим штанги узким хватом")
    )),
    WorkoutCategory(3, "Ноги", mutableListOf(
        Exercise("Приседания"),
        Exercise("Выпады"),
        Exercise("Подъем на носки с гантелями"),
        Exercise("Жим ногами на тренажере"),
        Exercise("Шаги с гантелями")
    )),
    WorkoutCategory(4,"Спина", mutableListOf(
        Exercise("Подтягивания"),
        Exercise("Широкое разведение рук с гантелями"),
        Exercise("Тяга к груди"),
        Exercise("Гиперэкстензия"),
        Exercise("Отведение рук с резиной")
    )),
    WorkoutCategory(5,"Растяжка", mutableListOf(
        Exercise("Дочка"),
        Exercise("Кобра"),
        Exercise("Глубокий выпад вперед"),
        Exercise("Поза мостика"),
        Exercise("Нисходящий собака")
    )),
    WorkoutCategory(6,"Пресс", mutableListOf(
        Exercise("Классические скручивания на пресс"),
        Exercise("Боковые скручивания"),
        Exercise("Велосипед"),
        Exercise("Планка"),
        Exercise("Упражнение \"Ножницы\"")
    )),
    WorkoutCategory(7,"Кардио-тренировки", mutableListOf(
        Exercise("Бег"),
        Exercise("Прыжки на скакалке"),
        Exercise("Велотренажер"),
        Exercise("Бёрпи"),
        Exercise("Плавание")
    ))
)