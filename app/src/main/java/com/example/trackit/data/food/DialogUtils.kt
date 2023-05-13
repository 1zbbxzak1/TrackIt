package com.example.trackit.ui.Food

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.trackit.R
import com.example.trackit.data.food.Globals
import com.example.trackit.data.food.ListFood
import com.example.trackit.ui.*
import com.example.trackit.ui.Nutrition.FoodAdapter
import com.example.trackit.ui.Nutrition.FoodData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.round
import kotlin.math.roundToInt

fun showAddDialog(food: FoodData, context: Context) {

    val builder = Dialog(context)
    builder.setContentView(R.layout.add_food_to_main)
    builder.window?.setBackgroundDrawableResource(android.R.color.transparent)

    val gramsEditText = builder.findViewById<EditText>(R.id.gramm)

    val nameTextView = builder.findViewById<TextView>(R.id.foodName2)
    nameTextView.text = food.name

    val proteinTextView = builder.findViewById<TextView>(R.id.protein_text2)
    proteinTextView.text = food.protein.toString()

    val fatTextView = builder.findViewById<TextView>(R.id.fat_text2)
    fatTextView.text = food.fat.toString()

    val carbsTextView = builder.findViewById<TextView>(R.id.carbs_text2)
    carbsTextView.text = food.carbs.toString()

    val caloriesTextView = builder.findViewById<TextView>(R.id.calories_text2)
    caloriesTextView.text = food.calories.toString()

    val addButton = builder.findViewById<Button>(R.id.add_button_food)
    var canAddFood = false // флаг, указывающий, можно ли добавить продукт

    val radioFood = builder.findViewById<RadioGroup>(R.id.radio_group_food)
    val breakfast = builder.findViewById<Button>(R.id.breakfast)
    val lunch = builder.findViewById<Button>(R.id.lunch)
    val dinner = builder.findViewById<Button>(R.id.dinner)
    val snack = builder.findViewById<Button>(R.id.snack)

    radioFood.setOnCheckedChangeListener { group, checkedId ->
        when (checkedId) {
            R.id.breakfast -> {
                setRadioButtonState(group, 0)
                lunch.background = null
                dinner.background = null
                snack.background = null
            }
            R.id.lunch -> {
                setRadioButtonState(group, 1)
                breakfast.background = null
                dinner.background = null
                snack.background = null
            }

            R.id.dinner -> {
                setRadioButtonState(group, 2)
                breakfast.background = null
                lunch.background = null
                snack.background = null
            }

            R.id.snack -> {
                setRadioButtonState(group, 3)
                breakfast.background = null
                lunch.background = null
                dinner.background = null
            }
        }

        // Установить цвет текста выбранной радиокнопки и сохранить его
        val checkedRadioButton = builder.findViewById<RadioButton>(checkedId)
        if (checkedRadioButton != null) {
            checkedRadioButton.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        // Снять флажки со всех остальных радиокнопок и установить для них черный цвет текста
        for (i in 0 until group.childCount) {
            val radioButton = group.getChildAt(i) as RadioButton
            if (radioButton.id != checkedId) {
                radioButton.setTextColor(ContextCompat.getColor(context, R.color.black))
                radioButton.isChecked = false
            }
        }
    }

    addButton.setOnClickListener {
        val gramsEntered = gramsEditText.text.toString().toIntOrNull() ?: 0
        if (gramsEntered <= 0) {
            Toast.makeText(context, "Пожалуйста, введите количество грамм", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }
        val factor = gramsEntered / 100.0 // отношение введенного количества грамм к 100 г
        val checkedId = radioFood.checkedRadioButtonId
        when (checkedId) {
            -1 -> {
                Toast.makeText(
                    context,
                    "Пожалуйста, выберите категорию для добавления продукта",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            else -> {
                val newFood = FoodData(
                    id = food.id,
                    name = food.name,
                    protein = round(food.protein * factor * 10.0) / 10.0,
                    fat = round(food.fat * factor * 10.0) / 10.0,
                    carbs = round(food.carbs * factor * 10.0) / 10.0,
                    calories = round(food.calories * factor * 10.0) / 10.0,
                    gramsEntered = gramsEntered
                )
                when (checkedId) {
                    R.id.breakfast -> {
                        ListFood.breakfastFoods.add(newFood)
                        Globals.TotalProteins += newFood.protein.roundToInt()
                        Globals.TotalFats += newFood.fat.roundToInt()
                        Globals.TotalCarbs += newFood.carbs.roundToInt()
                        Globals.TotalCalories += newFood.calories.roundToInt()
                    }
                    R.id.lunch -> {
                        ListFood.lunchFoods.add(newFood)
                        Globals.TotalProteins += newFood.protein.roundToInt()
                        Globals.TotalFats += newFood.fat.roundToInt()
                        Globals.TotalCarbs += newFood.carbs.roundToInt()
                        Globals.TotalCalories += newFood.calories.roundToInt()
                    }
                    R.id.dinner -> {
                        ListFood.dinnerFoods.add(newFood)
                        Globals.TotalProteins += newFood.protein.roundToInt()
                        Globals.TotalFats += newFood.fat.roundToInt()
                        Globals.TotalCarbs += newFood.carbs.roundToInt()
                        Globals.TotalCalories += newFood.calories.roundToInt()
                    }
                    R.id.snack -> {
                        ListFood.snackFoods.add(newFood)
                        Globals.TotalProteins += newFood.protein.roundToInt()
                        Globals.TotalFats += newFood.fat.roundToInt()
                        Globals.TotalCarbs += newFood.carbs.roundToInt()
                        Globals.TotalCalories += newFood.calories.roundToInt()
                    }
                }
                builder.dismiss()
            }
        }
    }

    val cancelButton = builder.findViewById<Button>(R.id.cancel_button2)
    cancelButton.setOnClickListener {
        builder.dismiss()
    }
    builder.show()
}

fun setupFoodList(
    context: Context,
    view: View,
    foodList: MutableList<FoodData>,
    foodAdapter: FoodAdapter) {
    val dialogAddButton = Dialog(context)
    dialogAddButton.setContentView(R.layout.dialog_add_food)

    // Кнопка для добавления продукта питания в общий список
    val addButton = view.findViewById<Button>(R.id.addButton)
    addButton.setOnClickListener {
        dialogAddButton.window?.setBackgroundDrawableResource(android.R.color.transparent) // удаление стандартного фона
        dialogAddButton.show()
    }

    // данные для создания нового продукта
    val nameEditText = dialogAddButton.findViewById<EditText>(R.id.name)
    val proteinEditText = dialogAddButton.findViewById<EditText>(R.id.protein_add)
    val fatEditText = dialogAddButton.findViewById<EditText>(R.id.fat_add)
    val carbsEditText = dialogAddButton.findViewById<EditText>(R.id.carbs_add)
    val caloriesEditText = dialogAddButton.findViewById<EditText>(R.id.calories_add)

    val sharedPreferences = context.getSharedPreferences("food_items", Context.MODE_PRIVATE)
    fun saveFoodItems() {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(foodList)
        editor.putString("food_items", json)
        editor.apply()
    }

    // кнопка Готово для окончательного добавления
    val addButton2 = dialogAddButton.findViewById<Button>(R.id.add_button_new_food)
    addButton2.setOnClickListener {
        val name = nameEditText.text.toString()
        val protein = proteinEditText.text.toString()
        val fat = fatEditText.text.toString()
        val carbs = carbsEditText.text.toString()
        val calories = caloriesEditText.text.toString()

        if (name.isNotEmpty() && protein.isNotEmpty() && fat.isNotEmpty() && carbs.isNotEmpty() && calories.isNotEmpty()) {
            // Создаем новый элемент
            val newFood = FoodData(0, name, protein.toDouble(), fat.toDouble(), carbs.toDouble(), calories.toDouble(), 0)

            // Добавляем элемент в список
            foodList.add(newFood)

            // Сохраняем данные
            saveFoodItems()

            // Обновляем адаптер
            foodAdapter.notifyDataSetChanged()

            // Закрываем диалоговое окно
            dialogAddButton.dismiss()
        } else {
            Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }

    // кнопка Отмена
    val cancelButton = dialogAddButton.findViewById<Button>(R.id.cancel_button)
    cancelButton.setOnClickListener {
        dialogAddButton.dismiss()
    }

    val gson = Gson()
    val json = sharedPreferences.getString("food_items", "")
    if (json?.isNotEmpty() == true) {
        val type = object : TypeToken<ArrayList<FoodData>>() {}.type
        val savedFoodList = gson.fromJson<ArrayList<FoodData>>(json, type)

        // Clear the existing list before adding saved items
        foodList.clear()

        // Add saved items to the list, skipping duplicates
        for (savedFood in savedFoodList) {
            if (!foodList.contains(savedFood)) {
                foodList.add(savedFood)
            }
        }
    }
}