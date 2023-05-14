package com.example.trackit.ui.Nutrition.Food

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.R
import com.example.trackit.data.Screen
import com.example.trackit.ui.Food.setupFoodList
import com.example.trackit.ui.Food.showAddDialog
import com.example.trackit.ui.Nutrition.FoodAdapter
import com.example.trackit.ui.Nutrition.FoodData
import com.example.trackit.ui.navigation.SearchView
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.BrightGray
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
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    var recyclerView: RecyclerView
    var foodAdapter: FoodAdapter

    val onFoodItemClickListener = object : FoodAdapter.OnFoodItemClickListener {
        override fun onFoodItemClick(food: FoodData) {
            showAddDialog(food, context, navigateToFoodPage)
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            LayoutInflater.from(context).inflate(R.layout.item_food, null)
        }
    ) { view ->
        recyclerView = view.findViewById(R.id.product_list)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)

        foodAdapter = FoodAdapter(foodList, context, onFoodItemClickListener)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = foodAdapter

//        // Функционал кнопки "Назад"
//        val prev = view.findViewById<Button>(R.id.previous)
//        prev.setOnClickListener { navigateBack() }

        // Функционал поиска продуктов
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                filterList(newText)
//                return true
//            }

//            fun filterList(query: String?) {
//                if (query != null) {
//                    val filteredList = ArrayList<FoodData>()
//                    for (i in NedoFoodList) {
//                        if (i.name.lowercase(Locale.ROOT).contains(query)) {
//                            filteredList.add(i)
//                        }
//                    }
//
//                    if (filteredList.isEmpty()) {
//                        Toast.makeText(context, "Продукт не найден", Toast.LENGTH_SHORT).show()
//                    } else {
//                        foodAdapter.setFilteredList(filteredList)
//                    }
//                }
//            }
//        })

        // добавление продукта в общий список
        setupFoodList(context, view, foodList, foodAdapter)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = navigateBack
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = BrightGray,
                    elevation = 8.dp,
                    modifier = Modifier.size(54.dp)
                ) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = "Назад",
                        modifier = Modifier
                            .size(40.dp)
                            .requiredSize(40.dp),
                        tint = Arsenic
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Card(
                modifier = Modifier,
                shape = RoundedCornerShape(20.dp),
                backgroundColor = Color(0xFFefefef),
                elevation = 15.dp
            ) {
                SearchView(state = searchQuery)
            }
        }
    }
}