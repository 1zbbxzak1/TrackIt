package com.example.trackit.ui

import android.view.LayoutInflater
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.trackit.R
import com.example.trackit.data.food.Globals
import com.example.trackit.ui.Nutrition.FoodData
import com.example.trackit.ui.theme.TrackItTheme
import java.time.LocalDate

@Composable
fun FoodPage(
    navigateToEntry: () -> Unit,
    selectedDate: LocalDate = LocalDate.now()
) {
    val context = LocalContext.current

    var breakfastExpanded by remember { mutableStateOf(false) }
    var lunchExpanded by remember { mutableStateOf(false) }
    var dinnerExpanded by remember { mutableStateOf(false) }
    var snackExpanded by remember { mutableStateOf(false) }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { contxt ->
            LayoutInflater.from(contxt).inflate(R.layout.activity_nutrition, null)
        },
        update = { view ->
            val pr = view.findViewById<TextView>(R.id.pr)
            pr.text = Globals.TotalProteins.toString()

            val fat = view.findViewById<TextView>(R.id.f)
            fat.text = Globals.TotalFats.toString()

            val carb = view.findViewById<TextView>(R.id.car)
            carb.text = Globals.TotalCarbs.toString()

            val cal = view.findViewById<TextView>(R.id.cal)
            cal.text = Globals.TotalCalories.toString()
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp)
    ) {
        item {
            MealPanel(
                mealType = "Завтрак",
                mealIcon = R.drawable.breakfast_icon,
                foods = breakfastFoods,
                isExpanded = breakfastExpanded,
                onPanelClicked = { breakfastExpanded = !breakfastExpanded },
                onAddButtonClick = { navigateToEntry() }
            )
            Spacer(modifier = Modifier.height(if (breakfastExpanded) 16.dp else 0.dp))
        }

        item {
            MealPanel(
                mealType = "Обед",
                mealIcon = R.drawable.lunch_icon,
                foods = lunchFoods,
                isExpanded = lunchExpanded,
                onPanelClicked = { lunchExpanded = !lunchExpanded },
                onAddButtonClick = { navigateToEntry() }
            )
            Spacer(modifier = Modifier.height(if (lunchExpanded) 16.dp else 0.dp))
        }

        item {
            MealPanel(
                mealType = "Ужин",
                mealIcon = R.drawable.dinner_icon,
                foods = dinnerFoods,
                isExpanded = dinnerExpanded,
                onPanelClicked = { dinnerExpanded = !dinnerExpanded },
                onAddButtonClick = { navigateToEntry() }
            )
            Spacer(modifier = Modifier.height(if (dinnerExpanded) 16.dp else 0.dp))
        }

        item {
            MealPanel(
                mealType = "Перекус",
                mealIcon = R.drawable.snack_icon,
                foods = snackFoods,
                isExpanded = snackExpanded,
                onPanelClicked = { snackExpanded = !snackExpanded },
                onAddButtonClick = { navigateToEntry() }
            )
        }
    }
}

@Composable
fun MealPanel(
    mealType: String,
    mealIcon: Int,
    foods: MutableList<FoodData>,
    isExpanded: Boolean,
    onPanelClicked: () -> Unit,
    onAddButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.clickable(onClick = onPanelClicked),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(2.dp)
            ) {
                Image(
                    painter = painterResource(id = mealIcon),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = mealType,
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    FoodCardList(foods)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun FoodCardList(foods: List<FoodData>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White
    ) {
        Column {
            for (i in foods.indices) {
                val food = foods[i]
                FoodCard(
                    food = food,
                    modifier = if (i == 0) {
                        Modifier.padding(16.dp)
                    } else {
                        Modifier
                            .padding(16.dp)
                            .padding(top = 0.dp)
                    }
                )
                if (i != foods.lastIndex) {
                    Divider(color = Color.LightGray)
                }
            }
        }
    }
}

@Composable
fun FoodCard(food: FoodData, modifier: Modifier) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        shape = RectangleShape
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = food.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "${food.gramsEntered} гр",
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

val breakfastFoods = mutableStateListOf<FoodData>()

val lunchFoods = mutableStateListOf<FoodData>()

val dinnerFoods = mutableStateListOf<FoodData>()

val snackFoods = mutableStateListOf<FoodData>()

@Preview(showBackground = true)
@Composable
fun PreviewFoodPage(){
    TrackItTheme {

    }
}