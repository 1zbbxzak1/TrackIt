package com.example.trackit.ui.statistics
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import com.github.tehras.charts.line.LineChartData
//import com.github.tehras.charts.line.LineChartData.Point
//import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
//import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
//import com.github.tehras.charts.line.renderer.point.HollowCircularPointDrawer
//import com.github.tehras.charts.line.renderer.point.NoPointDrawer
//import com.github.tehras.charts.line.renderer.point.PointDrawer
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
//            lineDrawer = SolidLineDrawer(),
//        )
//    )
//
//    var horizontalOffset by mutableStateOf(5f)
//    var pointDrawerType by mutableStateOf(PointDrawerType.Filled)
//    val pointDrawer: PointDrawer
//        get() {
//            return when (pointDrawerType) {
//                PointDrawerType.None -> NoPointDrawer
//                PointDrawerType.Filled -> FilledCircularPointDrawer()
//                PointDrawerType.Hollow -> HollowCircularPointDrawer()
//            }
//        }
//
//    private fun weightData(): Float = (100f * Math.random()).toFloat() + 45f
//
//    enum class PointDrawerType {
//        None,
//        Filled,
//        Hollow
//    }
//}