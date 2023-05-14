package com.example.trackit.ui.statistics
//
//import android.annotation.SuppressLint
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
//import androidx.compose.ui.unit.dp
//import com.example.trackit.ui.theme.Margins.horizontal
//import com.example.trackit.ui.theme.Margins.vertical
//import com.github.tehras.charts.line.LineChart
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
//            .height(150.dp)
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
//@Preview
//@Composable
//fun LineChartPreview() = LineChartScreen()