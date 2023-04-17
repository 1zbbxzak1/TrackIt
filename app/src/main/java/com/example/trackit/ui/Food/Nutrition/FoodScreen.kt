package com.example.trackit.ui.Nutrition.Food

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.R
import com.example.trackit.ui.Nutrition.FoodAdapter
import com.example.trackit.ui.Nutrition.FoodData
import values.NedoFoodList
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FoodScreen(
    navigateBack: () -> Unit,
    navigateToFoodPage: () -> Unit,
    selectedDate: LocalDate = LocalDate.now(),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var recyclerView: RecyclerView
    var foodAdapter: FoodAdapter
    var searchView : SearchView

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

            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_add_food)

            val foodList = NedoFoodList

            foodAdapter = FoodAdapter(foodList)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = foodAdapter

            val addButton = view.findViewById<Button>(R.id.addButton)
            addButton.setOnClickListener {
                dialog.show()
            }

            val nameEditText = dialog.findViewById<EditText>(R.id.name)
            val proteinEditText = dialog.findViewById<EditText>(R.id.protein_add)
            val fatEditText = dialog.findViewById<EditText>(R.id.fat_add)
            val carbsEditText = dialog.findViewById<EditText>(R.id.carbs_add)
            val caloriesEditText = dialog.findViewById<EditText>(R.id.calories_add)

            val addButton2 = dialog.findViewById<Button>(R.id.add_button)
            addButton2.setOnClickListener {
                val name = nameEditText.text.toString()
                val protein = proteinEditText.text.toString().toDouble()
                val fat = fatEditText.text.toString().toDouble()
                val carbs = carbsEditText.text.toString().toDouble()
                val calories = caloriesEditText.text.toString().toDouble()

                // Создаем новый элемент
                val newFood = FoodData(name, protein, fat, carbs, calories)

                // Добавляем элемент в список
                NedoFoodList.add(newFood)

                // Обновляем адаптер
                foodAdapter.notifyDataSetChanged()

                // Закрываем диалоговое окно
                dialog.dismiss()
            }

            val cancelButton = dialog.findViewById<Button>(R.id.cancel_button)
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            val prev = view.findViewById<Button>(R.id.previous)
            prev.setOnClickListener { navigateBack() }

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
                            Toast.makeText(context, "No Data found", Toast.LENGTH_SHORT).show()
                        } else {
                            foodAdapter.setFilteredList(filteredList)
                        }
                    }
                }
            })
        }
    }