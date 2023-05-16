package com.example.trackit.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackit.ui.theme.AndroidGreen
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.TrackItTheme
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.LineConfig
import com.himanshoe.charty.line.model.LineData

@Composable
fun StatChart(
    data : List<LineData>,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .size(400.dp, 300.dp)
    ) {
        Box(
            modifier = Modifier
                .size(400.dp, 300.dp)
        ){
            LineChart(
                lineData = data,
                colors = listOf(Color.Green, AndroidGreen),
                axisConfig = AxisConfig(
                    showAxis = true,
                    isAxisDashed = false,
                    showUnitLabels = true,
                    showXLabels = true,
                    xAxisColor = Arsenic,
                    yAxisColor = Arsenic
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExerciseGraphPreview(){
    TrackItTheme {
        val testData: List<LineData> = listOf( // TODO: Get data from DB
            LineData("11.05", 1F),
            LineData("12.05", 3F),
            LineData("13.05", 4F),
            LineData("14.05", 3F),
            LineData("15.05", 6F),
            LineData("15.05", 4F),
            LineData("15.05", 5F),
            LineData("15.05", 2F),
            LineData("15.05", 4F),
            LineData("15.05", 14F),
        )

        StatChart(data = testData)
    }
}