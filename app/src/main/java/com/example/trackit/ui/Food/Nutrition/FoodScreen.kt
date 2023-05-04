package com.example.trackit.ui.Nutrition.Food

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.R
import com.example.trackit.ui.BreakfastPanel
import com.example.trackit.ui.Food.showAddDialog
import com.example.trackit.ui.Nutrition.FoodAdapter
import com.example.trackit.ui.Nutrition.FoodData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import values.NedoFoodList
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FoodScreen(
    navigateBack: () -> Unit,
    navigateToFoodPage: () -> Unit
) {
    val context = LocalContext.current
    var recyclerView: RecyclerView
    var foodAdapter: FoodAdapter
    var searchView: SearchView

    val onFoodItemClickListener = object : FoodAdapter.OnFoodItemClickListener {
        override fun onFoodItemClick(food: FoodData) {
            showAddDialog(food, context)
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            LayoutInflater.from(context).inflate(R.layout.item_food, null)
        }
    ) { view ->
        recyclerView = view.findViewById(R.id.product_list)
        searchView = view.findViewById(R.id.searchView)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)


        val dialogAddButton = Dialog(context)
        dialogAddButton.setContentView(R.layout.dialog_add_food)

        val foodList = NedoFoodList

        foodAdapter = FoodAdapter(foodList, context, onFoodItemClickListener)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = foodAdapter

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
            val json = gson.toJson(NedoFoodList)
            editor.putString("food_items", json)
            editor.apply()
        }

        // кнопка Готово для окончательного добавления
        val addButton2 = dialogAddButton.findViewById<Button>(R.id.add_button_new_food)
        addButton2.setOnClickListener {
            val name = nameEditText.text.toString()
            val protein = proteinEditText.text.toString().toDouble()
            val fat = fatEditText.text.toString().toDouble()
            val carbs = carbsEditText.text.toString().toDouble()
            val calories = caloriesEditText.text.toString().toDouble()

            // Создаем новый элемент
            val newFood = FoodData(0, name, protein, fat, carbs, calories)

            // Добавляем элемент в список
            NedoFoodList.add(newFood)

            // Сохраняем данные
            saveFoodItems()

            // Обновляем адаптер
            foodAdapter.notifyDataSetChanged()

            // Закрываем диалоговое окно
            dialogAddButton.dismiss()
        }

        // кнопка Отмена
        val cancelButton = dialogAddButton.findViewById<Button>(R.id.cancel_button)
        cancelButton.setOnClickListener {
            dialogAddButton.dismiss()
        }

        // Функционал кнопки "Назад"
        val prev = view.findViewById<Button>(R.id.previous)
        prev.setOnClickListener { navigateBack() }

        // Функционал поиска продуктов
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

            fun filterList(query: String?) {
                if (query != null) {
                    val filteredList = ArrayList<FoodData>()
                    for (i in NedoFoodList) {
                        if (i.name.lowercase(Locale.ROOT).contains(query)) {
                            filteredList.add(i)
                        }
                    }

                    if (filteredList.isEmpty()) {
                        Toast.makeText(context, "Продукт не найден", Toast.LENGTH_SHORT).show()
                    } else {
                        foodAdapter.setFilteredList(filteredList)
                    }
                }
            }
        })

        val gson = Gson()
        val json = sharedPreferences.getString("food_items", "")
        if (json?.isNotEmpty() == true) {
            val type = object : TypeToken<ArrayList<FoodData>>() {}.type
            val savedFoodList = gson.fromJson<ArrayList<FoodData>>(json, type)

            // Clear the existing list before adding saved items
            NedoFoodList.clear()

            // Add saved items to the list, skipping duplicates
            for (savedFood in savedFoodList) {
                if (!NedoFoodList.contains(savedFood)) {
                    NedoFoodList.add(savedFood)
                }
            }
        }
    }
}