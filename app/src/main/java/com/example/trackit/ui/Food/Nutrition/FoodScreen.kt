package com.example.trackit.ui.Nutrition.Food

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackit.R
import com.example.trackit.data.food.*
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.Nutrition.FoodAdapter
import com.example.trackit.ui.Nutrition.FoodData
import com.example.trackit.ui.navigation.SearchView
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.BrightGray
import values.NedoFoodList
import java.time.LocalDate
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FoodScreen(
    navigateBack: () -> Unit,
    navigateToFoodPage: () -> Unit,
    selectedDate: LocalDate = LocalDate.now(),
    mealType: String?,
    breakfast: BreakfastViewModel = viewModel(factory = AppViewModelProvider.Factory),
    lunch: LunchViewModel = viewModel(factory = AppViewModelProvider.Factory),
    dinner: DinnerViewModel = viewModel(factory = AppViewModelProvider.Factory),
    snack: SnackViewModel = viewModel(factory = AppViewModelProvider.Factory),
    total: TotalViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    val context = LocalContext.current
    val foodList = NedoFoodList

    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    var recyclerView: RecyclerView
    var foodAdapter: FoodAdapter

    val onFoodItemClickListener = object : FoodAdapter.OnFoodItemClickListener {
        override fun onFoodItemClick(food: FoodData) {
            showAddDialog(food, context, navigateToFoodPage, mealType, selectedDate, breakfast, lunch, dinner, snack, total)
        }
    }

    val filteredFoodList = remember(searchQuery.value, foodList) {
        val query = searchQuery.value.text.lowercase(Locale.ROOT)
        if (query.isNotEmpty()) {
            foodList.filter { food ->
                food.name.lowercase(Locale.ROOT).contains(query)
            }.toMutableList()
        } else {
            foodList
        }
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

            SearchView(state = searchQuery)
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

            foodAdapter = FoodAdapter(filteredFoodList, context, onFoodItemClickListener)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = foodAdapter

            foodAdapter.setFilteredList(filteredFoodList, searchQuery.value.text)

            // добавление продукта в общий список
            setupFoodList(context, view, foodList)
        }
    }
}