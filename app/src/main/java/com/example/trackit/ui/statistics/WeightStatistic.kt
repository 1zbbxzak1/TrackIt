package com.example.trackit.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackit.ui.theme.Arsenic

@Composable
fun WeightStatistics(
    modifier: Modifier = Modifier,
){
    // TODO: загрузка данных (пары значение-дата)
    val weightData = listOf<Pair<String, String>>(Pair("65", "18:43, 05.06.2023"), Pair("66", "18:41, 05.06.2023"))

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GraphCard(
            modifier = Modifier.height(250.dp),
            data = listOf()
        ){ modifier, data ->
            LineGraph(modifier, data)
        }

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Arsenic, contentColor = Color.White)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "Добавить вес",
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                )
            )
        }

        Spacer(Modifier.height(30.dp))

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)){
            items(items = weightData){
                StatisticsCard(label = it.first, data = it.second)
            }
        }
    }
}