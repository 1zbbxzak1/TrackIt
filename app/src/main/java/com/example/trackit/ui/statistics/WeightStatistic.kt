package com.example.trackit.ui.statistics
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.background
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.example.trackit.ui.theme.Margins.horizontal
//import com.example.trackit.ui.theme.Margins.vertical
//import com.github.tehras.charts.line.LineChart
//import com.github.tehras.charts.line.LineChartData
//import com.github.tehras.charts.line.LineChartData.Point
//import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
//import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
//import com.github.tehras.charts.line.renderer.point.HollowCircularPointDrawer
//import com.github.tehras.charts.line.renderer.point.NoPointDrawer
//import com.github.tehras.charts.line.renderer.point.PointDrawer
//
//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@Composable
//fun LineChartScreen() {
//    LineChartScreenContent()
//}
//
//@Composable
//fun LineChartScreenContent() {
//    val lineChartDataModel = LineChartDataModel()
//
//    Column(
//        modifier = Modifier.padding(
//            horizontal = horizontal,
//            vertical = vertical
//        )
//    ) {
//        LineChartRow(lineChartDataModel)
//    }
//}
//
//@Composable
//fun LineChartRow(lineChartDataModel: LineChartDataModel) {
//    Box(
//        modifier = Modifier
//            .height(250.dp)
//            .fillMaxWidth()
//            .offset(y = 350.dp)
//    ) {
//
//        LineChart(
//            linesChartData = listOf(lineChartDataModel.lineChartData),
//            horizontalOffset = lineChartDataModel.horizontalOffset,
//            pointDrawer = lineChartDataModel.pointDrawer
//        )
//    }
//}
//
//class LineChartDataModel {
//    var lineChartData by mutableStateOf(
//        LineChartData(
//            points = listOf(
//                Point(weightData(), "1"),
//                Point(weightData(), "2"),
//                Point(weightData(), "3"),
//                Point(weightData(), "4"),
//                Point(weightData(), "5"),
//                Point(weightData(), "6"),
//                Point(weightData(), "7"),
//                Point(weightData(), "8"),
//                Point(weightData(), "9"),
//                Point(weightData(), "10"),
//            ),
//            lineDrawer = SolidLineDrawer(color = Color.Green),
//        )
//    )
//
//    var horizontalOffset by mutableStateOf(5f)
//    var pointDrawerType by mutableStateOf(PointDrawerType.Filled)
//    val pointDrawer: PointDrawer
//        get() {
//            val pointColor = when (pointDrawerType) {
//                PointDrawerType.None -> Color.Transparent
//                PointDrawerType.Filled -> Color.DarkGray // Set the desired point color
//                PointDrawerType.Hollow -> Color.Green // Set the desired point color
//            }
//            return when (pointDrawerType) {
//                PointDrawerType.None -> NoPointDrawer
//                PointDrawerType.Filled -> FilledCircularPointDrawer(color = pointColor)
//                PointDrawerType.Hollow -> HollowCircularPointDrawer(color = pointColor)
//            }
//        }
//
//    private fun weightData(): Float = (50 + Math.random()).toFloat() + 2f
//
//    enum class PointDrawerType {
//        None,
//        Filled,
//        Hollow
//    }
//}
//
//@Preview
//@Composable
//fun LineChartPreview() = LineChartScreen()