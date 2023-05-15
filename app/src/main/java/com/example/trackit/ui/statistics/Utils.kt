package com.example.trackit.ui.Profile
//
//import android.annotation.SuppressLint
//import com.github.mikephil.charting.components.AxisBase
//import com.github.mikephil.charting.formatter.ValueFormatter
//import java.text.ParseException
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.math.roundToInt
//
//class ClaimsXAxisValueFormatter(private val datesList: List<String>) : ValueFormatter() {
//
//    @SuppressLint("SimpleDateFormat")
//    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//        var position = value.roundToInt()
//        val sdf = SimpleDateFormat("MMM dd")
//
//        when {
//            value > 1 && value < 2 -> position = 0
//            value > 2 && value < 3 -> position = 1
//            value > 3 && value < 4 -> position = 2
//            value > 4 && value <= 5 -> position = 3
//        }
//
//        if (position < datesList.size)
//            return sdf.format(Date(getDateInMilliSeconds(datesList[position], "yyyy-MM-dd")))
//        return ""
//    }
//
//    fun getDateInMilliSeconds(givenDateString: String, format: String): Long {
//        val dateTimeFormat = format
//        val sdf = SimpleDateFormat(dateTimeFormat, Locale.US)
//        var timeInMilliseconds: Long = 1
//        try {
//            val mDate = sdf.parse(givenDateString)
//            timeInMilliseconds = mDate?.time ?: 1
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        return timeInMilliseconds
//    }
//}
//
//class ClaimsYAxisValueFormatter : ValueFormatter() {
//    override fun getAxisLabel(value: Float, axis: AxisBase): String {
//        return value.toString()
//    }
//}