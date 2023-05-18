import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.TrackItTheme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.datetime.LocalDate

@Composable
fun StatChart(
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

                val chartData = prepareChartData(data)

                setData(chartData)
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    axisLineColor = Arsenic.toArgb()
                    setDrawGridLines(false)
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val index = value.toInt()
                            if (index in data.indices) {
                                return data[index].second
                            }
                            return ""
                        }
                    }
                    labelCount = data.count()
                    granularity = 1f
                }

                val axisLeft = axisLeft

                axisLeft.axisLineColor = Arsenic.toArgb()
                axisLeft.setDrawGridLines(true)
                axisLeft.valueFormatter = DefaultValueFormatter(0)
                axisLeft.labelCount = data.count()
                axisLeft.granularity = 1f

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
            }
        })
    }
}

private fun prepareChartData(data: List<Pair<Float, String>>): LineData {
    val entries = data.mapIndexed { index, pair ->
        Entry(index.toFloat(), pair.first)
    }
    val dataSet = LineDataSet(entries, "").apply {
        color = Color.GREEN
        valueTextColor = Color.BLACK
        lineWidth = 1.5f
        setDrawCircles(true)
        circleRadius = 4f
        circleHoleRadius = 2f
        setCircleColor(Color.GREEN)
        setDrawValues(false)
        mode = LineDataSet.Mode.HORIZONTAL_BEZIER
    }
    return LineData(listOf<ILineDataSet>(dataSet))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExerciseGraphPreview() {
    TrackItTheme {
        val testData: List<Pair<Float, String>> = listOf(
            1F to "11.05",
            3F to "12.05",
            4F to "13.05",
            3F to "14.05",
            6F to "15.05",
        )

        StatChart(data = testData)
    }
}
