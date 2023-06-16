package com.example.trackit.ui.statistics

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trackit.R
import com.example.trackit.data.weight.WeightViewModel
import com.example.trackit.ui.AppViewModelProvider
import com.example.trackit.ui.theme.AndroidGreen
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.CaptionColor
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

@Composable
fun GraphCard(
    modifier: Modifier = Modifier,
    data: List<Pair<Float, String>>,
    graph: @Composable (Modifier, List<Pair<Float, String>>) -> Unit
){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        elevation = 6.dp,
        backgroundColor = Arsenic
    ) {
        if (data.isEmpty()){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Используйте приложение,\nчтобы появилась статистика",
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    softWrap = false,
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        color = CaptionColor
                    )
                )
            }
        }
        else {
            graph(
                Modifier
                    .height(600.dp)
                    .padding(5.dp),
                data.ifEmpty { listOf() }
            )
        }
    }
}

@Composable
fun LineGraph(
    modifier: Modifier = Modifier,
    data: List<Pair<Float, String>>
) {
    Box {
        AndroidView(modifier = modifier,
            factory = { context ->
                LineChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    val chartData = prepareLineGraphData(data, context)

                    setData(if (chartData.entryCount > 0) chartData else null)

                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.axisLineColor = Color.WHITE
                    xAxis.setDrawGridLines(false)
                    xAxis.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val index = value.toInt()
                            if (index in data.indices) {
                                return data[index].second
                            }
                            return ""
                        }
                    }

                    xAxis.textColor = Color.WHITE
                    xAxis.labelCount = data.count()
                    xAxis.granularity = 1f

                    axisLeft.axisLineColor = Color.WHITE
                    axisLeft.setDrawGridLines(true)
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
                    setPadding(20, 160, 20, 160)
                    setBackgroundColor(Arsenic.toArgb())

                    // No data text
                    setNoDataText("Используйте приложение, чтобы появилась статистика")
                    setNoDataTextColor(CaptionColor.toArgb())
                    setNoDataTextTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
                    getPaint(Chart.PAINT_INFO).textSize = 36f;
                }
            })
    }
}

private fun prepareLineGraphData(data: List<Pair<Float, String>>, context: Context): LineData {
    val entries = data.mapIndexed { index, pair ->
        Entry(index.toFloat(), pair.first)
    }
    val dataLine = LineDataSet(entries, "")

    dataLine.color = AndroidGreen.toArgb()
    dataLine.valueTextColor = Color.BLACK
    dataLine.lineWidth = 1f
    dataLine.setDrawCircles(true)
    dataLine.circleRadius = 5f
    dataLine.circleHoleRadius = 3f
    dataLine.setCircleColor(AndroidGreen.toArgb())
    dataLine.setDrawValues(false)
    dataLine.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
    dataLine.setDrawFilled(true)
    dataLine.fillDrawable = ContextCompat.getDrawable(context, R.drawable.fade_green)

    return LineData(listOf<ILineDataSet>(dataLine))
}

@Composable
fun WeightLineGraph(
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

                    val chartData = prepareWeightGraphData(data, context)

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
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }
    }
}

private fun prepareWeightGraphData(data: List<Pair<Float, String>>, context: Context): LineData {
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

