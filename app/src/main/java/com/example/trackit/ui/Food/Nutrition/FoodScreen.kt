package com.example.trackit.ui.Nutrition.Food

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.R
import com.example.trackit.ui.Food.setupFoodList
import com.example.trackit.ui.Food.showAddDialog
import com.example.trackit.ui.Nutrition.FoodAdapter
import com.example.trackit.ui.Nutrition.FoodData
import values.NedoFoodList
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FoodScreen(
    navigateBack: () -> Unit,
    navigateToFoodPage: () -> Unit
) {
    val context = LocalContext.current
    val foodList = NedoFoodList
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

        foodAdapter = FoodAdapter(foodList, context, onFoodItemClickListener)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = foodAdapter

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

        // добавление продукта в общий список
        setupFoodList(context, view, foodList, foodAdapter)
    }
}