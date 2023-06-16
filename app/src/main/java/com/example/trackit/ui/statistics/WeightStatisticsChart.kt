package com.example.trackit.ui.statistics

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.data.Weight.WeightViewModel
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.theme.AndroidGreen
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.CaptionColor
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter

@Composable
fun WeightStatisticsChart(
    data: List<Pair<Float, String>>,
    modifier: Modifier = Modifier,
    weightViewModel: WeightViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by weightViewModel.weightUiState.collectAsState()

    Box {
        AndroidView(modifier = modifier,
            factory = { context ->
                LineChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    val chartData = prepareChartData(data, context)

                    setData(if (chartData.entryCount > 0.0) chartData else null)

                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setDrawAxisLine(true)
                    xAxis.axisLineColor = Color.WHITE
                    xAxis.setDrawGridLines(false)
                    xAxis.setDrawLabels(false)
                    xAxis.textColor = Color.WHITE
                    xAxis.labelCount = data.size
                    xAxis.granularity = 1f

                    axisLeft.axisLineColor = Color.WHITE
                    xAxis.setDrawLabels(false)
                    xAxis.setDrawGridLines(false)
                    axisLeft.valueFormatter = DefaultValueFormatter(0)
                    axisLeft.labelCount = data.count()
                    axisLeft.granularity = 1f
                    axisLeft.textColor = Color.WHITE

                    axisRight.isEnabled = false
                    description.isEnabled = false
                    legend.isEnabled = false
                    setTouchEnabled(false)
                    animateXY(300, 300)
                    setDrawBorders(true)
                    setBorderColor(Arsenic.toArgb())
                    setNoDataText("")
                    setNoDataTextColor(CaptionColor.toArgb())
                    setNoDataTextTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
                    setPadding(40, 40, 40, 40)
                    setBackgroundColor(Arsenic.toArgb())
                }
            })

        if (uiState.weightList.isNotEmpty()) {
            Text(
                text = "Последние 10 замеров",
                color = White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }
    }
}

private fun prepareChartData(data: List<Pair<Float, String>>, context: Context): LineData {
    val entries = data.mapIndexed { index, pair -> Entry(index.toFloat(), pair.first) }

    val dataLine = LineDataSet(entries, "").apply {
        color = AndroidGreen.toArgb()
        valueTextColor = Color.BLACK
        lineWidth = 2f
        setDrawCircles(true)
        circleRadius = 5f
        circleHoleRadius = 3f
        setCircleColor(AndroidGreen.toArgb())
        setDrawValues(false)
        mode = LineDataSet.Mode.LINEAR
        setDrawFilled(false)
    }

    return LineData(listOf(dataLine))
}