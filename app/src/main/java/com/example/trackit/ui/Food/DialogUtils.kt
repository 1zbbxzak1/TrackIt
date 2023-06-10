package com.example.trackit.data.food

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.R
import com.example.trackit.data.Weight.WeightEntry
import com.example.trackit.data.Weight.WeightViewModel
import com.example.trackit.data.workout.WorkoutCategory
import com.example.trackit.ui.*
import com.example.trackit.ui.Nutrition.FoodAdapter
import com.example.trackit.ui.Nutrition.FoodData
import com.example.trackit.ui.theme.*
import com.example.trackit.ui.workout.AddDeleteButton
import com.example.trackit.ui.workout.DialogTextField
import com.example.trackit.ui.workout.category.WorkoutCategoryViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round
import kotlin.math.roundToInt

fun showAddDialog(
    food: FoodData,
    context: Context,
    navigateToFoodPage: () -> Unit,
    selectedDate: LocalDate = LocalDate.now()
) {
    val log = getLogForDate(selectedDate)
    val builder = Dialog(context)
    builder.setContentView(R.layout.add_food_to_main)
    builder.window?.setBackgroundDrawableResource(android.R.color.transparent)

    val gramsEditText = builder.findViewById<EditText>(R.id.gramm)
    gramsEditText.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            gramsEditText.clearFocus()
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(gramsEditText.windowToken, 0)
        }
        false
    }

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

        when (val checkedId = radioFood.checkedRadioButtonId) {
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
                    id = generateUniqueID(),
                    name = food.name,
                    protein = round(food.protein * factor * 10.0) / 10.0,
                    fat = round(food.fat * factor * 10.0) / 10.0,
                    carbs = round(food.carbs * factor * 10.0) / 10.0,
                    calories = round(food.calories * factor * 10.0) / 10.0,
                    gramsEntered = gramsEntered
                )
                when (checkedId) {
                    R.id.breakfast -> {
                        ListFood.logs.add(log)
                        log.breakfastFoods.add(newFood)
                        log.totalProteins += newFood.protein.roundToInt()
                        log.totalFats += newFood.fat.roundToInt()
                        log.totalCarbs += newFood.carbs.roundToInt()
                        log.totalCalories += newFood.calories.roundToInt()
                    }
                    R.id.lunch -> {
                        ListFood.logs.add(log)
                        log.lunchFoods.add(newFood)
                        log.totalProteins += newFood.protein.roundToInt()
                        log.totalFats += newFood.fat.roundToInt()
                        log.totalCarbs += newFood.carbs.roundToInt()
                        log.totalCalories += newFood.calories.roundToInt()
                    }
                    R.id.dinner -> {
                        ListFood.logs.add(log)
                        log.dinnerFoods.add(newFood)
                        log.totalProteins += newFood.protein.roundToInt()
                        log.totalFats += newFood.fat.roundToInt()
                        log.totalCarbs += newFood.carbs.roundToInt()
                        log.totalCalories += newFood.calories.roundToInt()
                    }
                    R.id.snack -> {
                        ListFood.logs.add(log)
                        log.snackFoods.add(newFood)
                        log.totalProteins += newFood.protein.roundToInt()
                        log.totalFats += newFood.fat.roundToInt()
                        log.totalCarbs += newFood.carbs.roundToInt()
                        log.totalCalories += newFood.calories.roundToInt()
                    }
                }
                builder.dismiss()
                navigateToFoodPage()
            }
        }
    }

    val cancelButton = builder.findViewById<Button>(R.id.cancel_button2)
    cancelButton.setOnClickListener {
        builder.dismiss()
    }
    builder.show()
}

@SuppressLint("NotifyDataSetChanged")
fun setupFoodList(
    context: Context,
    view: View,
    foodList: MutableList<FoodData>,
    foodAdapter: FoodAdapter
) {
    val dialogAddButton = Dialog(context)
    dialogAddButton.setContentView(R.layout.dialog_add_food)

    // Кнопка для добавления продукта питания в общий список
    val addButton = view.findViewById<Button>(R.id.addButton)
    addButton.setOnClickListener {
        dialogAddButton.window?.setBackgroundDrawableResource(android.R.color.transparent) // удаление стандартного фона
        dialogAddButton.show()
    }

    val addButton1 = view.findViewById<Button>(R.id.create_food)
    addButton1.setOnClickListener {
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
            val newFood = FoodData(generateUniqueID(), name, protein.toDouble(), fat.toDouble(), carbs.toDouble(), calories.toDouble(), 0)

            // Добавляем элемент в список
            foodList.add(newFood)

            // Сохраняем данные
            saveFoodItems()

            // Обновляем адаптер
            foodAdapter.setFilteredList(foodList)

            // Закрываем диалоговое окно
            dialogAddButton.dismiss()

            // Сбросить введенные значения
            nameEditText.text.clear()
            proteinEditText.text.clear()
            fatEditText.text.clear()
            carbsEditText.text.clear()
            caloriesEditText.text.clear()

            nameEditText.clearFocus()
            proteinEditText.clearFocus()
            fatEditText.clearFocus()
            carbsEditText.clearFocus()
            caloriesEditText.clearFocus()

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

@Composable
fun AddWeightDialog(
    onAddWeight: (String) -> Unit,
    onDismiss: () -> Unit,
    selectedDate: LocalDate = LocalDate.now()
) {
    var weight by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(25.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Добавить вес", style = DialogTextStyle, maxLines = 1)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Dialog(
                        value = weight,
                        onValueChange = { weight = it },
                        label = "Вес",
                        modifier = Modifier.height(60.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        AddDeleteButton(
                            text = "Отмена",
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Arsenic),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        AddDeleteButton(
                            text = "Готово",
                            onClick = {
                                onAddWeight(weight)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Arsenic,
                                disabledBackgroundColor = Arsenic,
                                disabledContentColor = Color.White,
                                contentColor = AndroidGreen
                            ),
                            enabled = weight.isNotBlank(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Dialog(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    caption: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    placeHolder: String = "",
    shape: Shape = RoundedCornerShape(20.dp),
    modifier: Modifier = Modifier
){
    val numericKeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    BasicTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = {onValueChange(it)},
        textStyle = TextFieldTextStyle,
        maxLines = 1,
        singleLine = true,
        keyboardOptions = numericKeyboardOptions,
        keyboardActions = keyboardActions
    ) {innerTextField ->
        Card(
            shape = shape,
            backgroundColor = Color.White,
            elevation = 6.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = label, style = TextFieldLabelTextStyle,
                modifier = Modifier.padding(start = 20.dp)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()){
                            Text(
                                text = placeHolder,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Light,
                                color = BrightGray
                            )
                        }
                        innerTextField()
                    }
                    Text(text = caption, style = TextFieldTextStyle)
                }
            }
        }
    }
}