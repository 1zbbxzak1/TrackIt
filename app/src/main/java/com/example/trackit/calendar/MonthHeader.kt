package com.example.trackit.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.trackit.R
import com.example.trackit.ui.theme.Arsenic
import com.example.trackit.ui.theme.CalendarDayTextStyle
import io.github.boguszpawlowski.composecalendar.header.MonthState
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MonthHeader(
    monthState: MonthState,
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        color = MaterialTheme.colors.primaryVariant
    ) {
        Column(
            modifier = modifier
                .background(Arsenic),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.height(14.dp), contentAlignment = Alignment.Center){
                Icon(
                    painterResource(id = R.drawable.expand_icon), contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.width(41.dp)
                )
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Text(
                    text = monthState.currentMonth.month
                        .getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
                        .lowercase()
                        .replaceFirstChar { it.titlecase() } + " " + monthState.currentMonth.year.toString(),
                    style = CalendarDayTextStyle,
                    modifier = Modifier.padding(start = 30.dp),
                    color = Color.White
                )
            }
        }
    }
}