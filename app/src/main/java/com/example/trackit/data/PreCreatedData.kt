package values

import com.example.trackit.ui.workout.Exercise
import com.example.trackit.ui.workout.WorkoutCategory

val preCreatedCategoryList = listOf(
    WorkoutCategory(0, "Грудные мышцы", listOf(
        Exercise("Жим штанги лежа"),
        Exercise("Жим гантелей лежа"),
        Exercise("Жим гантелей на наклонной скамье"),
        Exercise("Разводка гантелей на горизонтальной скамье"),
        Exercise("Отжимания от пола")
    )),
    WorkoutCategory(1, "Руки", listOf(
        Exercise("Жим штанги на бицепс"),
        Exercise("Сгибания рук со гантелями"),
        Exercise("Отжимания на брусьях"),
        Exercise("Молотковый подъем"),
        Exercise("Упражнение \"Бритва\"")
    )),
    WorkoutCategory(2, "Плечи", listOf(
        Exercise("Жим гантелей стоя"),
        Exercise("Французский жим"),
        Exercise("Подтягивания к подбородку"),
        Exercise("Вращения гантелей наружу и внутрь"),
        Exercise("Жим штанги узким хватом")
    )),
    WorkoutCategory(3, "Ноги", listOf(
        Exercise("Приседания"),
        Exercise("Выпады"),
        Exercise("Подъем на носки с гантелями"),
        Exercise("Жим ногами на тренажере"),
        Exercise("Шаги с гантелями")
    )),
    WorkoutCategory(4,"Спина", listOf(
        Exercise("Подтягивания"),
        Exercise("Широкое разведение рук с гантелями"),
        Exercise("Тяга к груди"),
        Exercise("Гиперэкстензия"),
        Exercise("Отведение рук с резиной")
    )),
    WorkoutCategory(5,"Растяжка", listOf(
        Exercise("Дочка"),
        Exercise("Кобра"),
        Exercise("Глубокий выпад вперед"),
        Exercise("Поза мостика"),
        Exercise("Нисходящий собака")
    )),
    WorkoutCategory(6,"Пресс", listOf(
        Exercise("Классические скручивания на пресс"),
        Exercise("Боковые скручивания"),
        Exercise("Велосипед"),
        Exercise("Планка"),
        Exercise("Упражнение \"Ножницы\"")
    )),
    WorkoutCategory(7,"Кардио-тренировки", listOf(
        Exercise("Бег"),
        Exercise("Прыжки на скакалке"),
        Exercise("Велотренажер"),
        Exercise("Бёрпи"),
        Exercise("Плавание")
    ))
)