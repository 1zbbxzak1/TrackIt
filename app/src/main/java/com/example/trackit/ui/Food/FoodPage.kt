package com.example.trackit.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.trackit.R
import com.example.trackit.ui.Food.Constants
import com.example.trackit.ui.Nutrition.ExpandedPanel
import com.example.trackit.ui.Nutrition.FoodData
import com.example.trackit.ui.theme.TrackItTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun FoodPage(
    navigateToEntry: () -> Unit
) {

    val context = LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { contxt ->
            LayoutInflater.from(contxt).inflate(R.layout.activity_nutrition, null)
        }
    )

    val breakfastFoods = loadBreakfastFoods(context)

    var breakfastExpanded by remember { mutableStateOf(false) }
    var lunchExpanded by remember { mutableStateOf(false) }
    var dinnerExpanded by remember { mutableStateOf(false) }
    var snackExpanded by remember { mutableStateOf(false) }

    var expandedPanel by remember { mutableStateOf<ExpandedPanel?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp)
    ) {
        item {
            BreakfastPanel(
                isExpanded = expandedPanel == ExpandedPanel.Breakfast,
                onPanelClicked = {
                    expandedPanel = if (expandedPanel == ExpandedPanel.Breakfast) null else ExpandedPanel.Breakfast
                },
                onAddButtonClick = { navigateToEntry() }
            )
            Spacer(modifier = Modifier.height(if (breakfastExpanded) 16.dp else 0.dp))
        }

        item {
            LunchPanel(
                isExpanded = expandedPanel == ExpandedPanel.Lunch,
                onPanelClicked = {
                    expandedPanel = if (expandedPanel == ExpandedPanel.Lunch) null else ExpandedPanel.Lunch
                },
                onAddButtonClick = { navigateToEntry() }
            )
            Spacer(modifier = Modifier.height(if (lunchExpanded) 16.dp else 0.dp))
        }

        item {
            DinnerPanel(
                isExpanded = expandedPanel == ExpandedPanel.Dinner,
                onPanelClicked = {
                    expandedPanel = if (expandedPanel == ExpandedPanel.Dinner) null else ExpandedPanel.Dinner
                },
                onAddButtonClick = { navigateToEntry() }
            )
            Spacer(modifier = Modifier.height(if (dinnerExpanded) 16.dp else 0.dp))
        }

        item {
            SnackPanel(
                isExpanded = expandedPanel == ExpandedPanel.Snack,
                onPanelClicked = {
                    expandedPanel = if (expandedPanel == ExpandedPanel.Snack) null else ExpandedPanel.Snack
                },
                onAddButtonClick = { navigateToEntry() }
            )
        }
    }
}

val breakfastFoods = mutableListOf<FoodData>()
fun addFoodToBreakfastList(food: FoodData) {
    breakfastFoods.addAll(listOf(food))
}

val lunchFoods = mutableListOf<FoodData>()
fun addFoodToLunchList(food: FoodData) {
    lunchFoods.addAll(listOf(food))
}

val dinnerFoods = mutableListOf<FoodData>()
fun addFoodToDinnerList(food: FoodData) {
    dinnerFoods.addAll(listOf(food))
}

val snackFoods = mutableListOf<FoodData>()
fun addFoodToSnackList(food: FoodData) {
    snackFoods.addAll(listOf(food))
}

fun saveBreakfastFoods(context: Context, foods: List<FoodData>) {
    val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
    val editor = prefs.edit()
    val gson = Gson()
    val jsonFoods = gson.toJson(foods)
    editor.putString(Constants.BREAKFAST_FOODS, jsonFoods)
    editor.apply()
}

fun loadBreakfastFoods(context: Context): List<FoodData> {
    val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
    val jsonFoods = prefs.getString(Constants.BREAKFAST_FOODS, null)
    if (jsonFoods != null) {
        val gson = Gson()
        val type = object : TypeToken<List<FoodData>>() {}.type
        return gson.fromJson(jsonFoods, type)
    }
    return emptyList()
}



@Composable
fun BreakfastPanel(
    isExpanded: Boolean,
    onPanelClicked: () -> Unit,
    onAddButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.clickable(onClick = onPanelClicked)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(2.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.breakfast_icon),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Завтрак",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onAddButtonClick,
                    modifier = Modifier.size(50.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            if (isExpanded) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    for (food in breakfastFoods) {
                        FoodCard(food = food)
                    }
                }
            }
            }
        }
    }

@Composable
fun LunchPanel(
    isExpanded: Boolean,
    onPanelClicked: () -> Unit,
    onAddButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.clickable(onClick = onPanelClicked)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(2.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lunch_icon),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Обед",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onAddButtonClick,
                    modifier = Modifier.size(50.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            if (isExpanded) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    for (food in lunchFoods) {
                        FoodCard(food = food)
                    }
                }
            }
        }
    }
}

@Composable
fun DinnerPanel(
    isExpanded: Boolean,
    onPanelClicked: () -> Unit,
    onAddButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.clickable(onClick = onPanelClicked)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(2.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dinner_icon),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Ужин",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onAddButtonClick,
                    modifier = Modifier.size(50.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            if (isExpanded) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    for (food in dinnerFoods) {
                        FoodCard(food = food)
                    }
                }
            }
        }
    }
}

@Composable
fun SnackPanel(
    isExpanded: Boolean,
    onPanelClicked: () -> Unit,
    onAddButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.clickable(onClick = onPanelClicked)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(2.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.snack_icon),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Перекус",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onAddButtonClick,
                    modifier = Modifier.size(50.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            if (isExpanded) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    for (food in snackFoods) {
                        FoodCard(food = food)
                    }
                }
            }
        }
    }
}

@Composable
fun FoodCard(food: FoodData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            val sh = FontFamily(Typeface.SANS_SERIF)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = food.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "100 гр",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(android.graphics.Color.parseColor("#99cd4e"))
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${food.protein}", fontSize = 16.sp, modifier = Modifier.padding(start = 20.dp))
                Text(text = "${food.fat}", fontSize = 16.sp, modifier = Modifier.padding(start = 0.dp))
                Text(text = "${food.carbs}", fontSize = 16.sp, modifier = Modifier.padding(start = 0.dp))
                Text(text = "${food.calories}", fontSize = 16.sp, modifier = Modifier.padding(start = 0.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFoodPage(){
    TrackItTheme {

    }
}


//
//        val button2 = view.findViewById<Button>(R.id.add_button2)
//        button2.setOnClickListener {
//            buttonClicked.value = "Button 2"
//            navigateToEntry()
//        }
//
//        val button3 = view.findViewById<Button>(R.id.add_button3)
//        button3.setOnClickListener {
//            buttonClicked.value = "Button 3"
//            navigateToEntry()
//        }
//
//        val button4 = view.findViewById<Button>(R.id.add_button4)
//        button4.setOnClickListener {
//            buttonClicked.value = "Button 4"
//            navigateToEntry()
//        }
//
//        // общий счетчик КБЖУ
//        val proteins = view.findViewById<TextView>(R.id.pr)
//        val fats = view.findViewById<TextView>(R.id.f)
//        val carbs = view.findViewById<TextView>(R.id.car)
//        val calories = view.findViewById<TextView>(R.id.cal)