package com.example.trackit.ui.Food

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.R
import com.example.trackit.ui.*
import com.example.trackit.ui.Nutrition.FoodData

fun showAddDialog(food: FoodData, context: Context) {

    val builder = Dialog(context)
    builder.setContentView(R.layout.add_food_to_main)
    builder.window?.setBackgroundDrawableResource(android.R.color.transparent)

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

    val radioFood = builder.findViewById<RadioGroup>(R.id.radio_group_food)
    val breakfast = builder.findViewById<Button>(R.id.breakfast)
    val lunch = builder.findViewById<Button>(R.id.lunch)
    val dinner = builder.findViewById<Button>(R.id.dinner)
    val snack = builder.findViewById<Button>(R.id.snack)

    radioFood.setOnCheckedChangeListener { group, checkedId ->
        when (checkedId) {
            R.id.breakfast -> {
                com.example.trackit.ui.setRadioButtonState(group, 0)
                lunch.background = null
                dinner.background = null
                snack.background = null
            }
            R.id.lunch -> {
                com.example.trackit.ui.setRadioButtonState(group, 1)
                breakfast.background = null
                dinner.background = null
                snack.background = null
            }

            R.id.dinner -> {
                com.example.trackit.ui.setRadioButtonState(group, 2)
                breakfast.background = null
                lunch.background = null
                snack.background = null
            }

            R.id.snack -> {
                com.example.trackit.ui.setRadioButtonState(group, 3)
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
        val checkedId = radioFood.checkedRadioButtonId
        when (checkedId) {
            R.id.breakfast -> {
                val newFood = FoodData(
                    name = food.name,
                    protein = food.protein,
                    fat = food.fat,
                    carbs = food.carbs,
                    calories = food.calories
                )

                addFoodToBreakfastList(newFood)
                saveBreakfastFoods(context, breakfastFoods)
                builder.dismiss()
            }
            R.id.lunch -> {
                val newFood = FoodData(
                    name = food.name,
                    protein = food.protein,
                    fat = food.fat,
                    carbs = food.carbs,
                    calories = food.calories
                )

                addFoodToLunchList(newFood)
//                saveBreakfastFoods(context, breakfastFoods)
                builder.dismiss()
            }
            R.id.dinner -> {
                val newFood = FoodData(
                    name = food.name,
                    protein = food.protein,
                    fat = food.fat,
                    carbs = food.carbs,
                    calories = food.calories
                )

                addFoodToDinnerList(newFood)
//                saveBreakfastFoods(context, breakfastFoods)
                builder.dismiss()
            }
            R.id.snack -> {
                val newFood = FoodData(
                    name = food.name,
                    protein = food.protein,
                    fat = food.fat,
                    carbs = food.carbs,
                    calories = food.calories
                )

                addFoodToSnackList(newFood)
//                saveBreakfastFoods(context, breakfastFoods)
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