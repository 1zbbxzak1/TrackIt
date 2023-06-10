package com.example.trackit.ui.statistics

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.trackit.R
import com.example.trackit.ui.theme.AndroidGreen
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.TrackItTheme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

@Composable
fun StatsChartForW(
    data: List<Pair<Float, String>>,
    modifier: Modifier = Modifier
) {
    Box {
        AndroidView(modifier = modifier,
            factory = { context ->
                LineChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    val chartData = prepareChartData(data, context)

                    setData(chartData)

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
                    setNoDataText("No data available")
                    setNoDataTextColor(Color.BLACK)
                    setNoDataTextTypeface(Typeface.DEFAULT)
                    setPadding(40, 40, 40, 40)
                    setBackgroundColor(Arsenic.toArgb())
                }
            })

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

private fun prepareChartData(data: List<Pair<Float, String>>, context: Context): LineData {
    val entries = data.mapIndexed { index, pair ->
        Entry(index.toFloat(), pair.first)
    }
    val dataLine = LineDataSet(entries, "")

    dataLine.color = AndroidGreen.toArgb()
    dataLine.valueTextColor = Color.BLACK
    dataLine.lineWidth = 2f
    dataLine.setDrawCircles(true)
    dataLine.circleRadius = 5f
    dataLine.circleHoleRadius = 3f
    dataLine.setCircleColor(AndroidGreen.toArgb())
    dataLine.setDrawValues(false)
    dataLine.mode = LineDataSet.Mode.LINEAR
    dataLine.setDrawFilled(false)

    return LineData(listOf<ILineDataSet>(dataLine))
}