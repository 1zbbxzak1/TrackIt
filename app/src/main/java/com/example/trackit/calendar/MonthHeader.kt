package com.example.trackit.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackit.ui.theme.TrackItTheme
import io.github.boguszpawlowski.composecalendar.header.MonthState
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun MonthHeader(
    monthState: MonthState,
    modifier: Modifier = Modifier
){
    Row {
        Surface(modifier = modifier.weight(1f).padding(bottom = 2.dp)) {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = monthState.currentMonth.month
                    .getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
                    .lowercase()
                    .replaceFirstChar { it.titlecase() },
                    style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 3.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = monthState.currentMonth.year.toString(), style = MaterialTheme.typography.h6)
            }
        }
        Surface() {
            ExpandIcon(expanded = true)
        }
        Spacer(modifier = modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun MonthHeaderPreview(){
    TrackItTheme {
        MonthHeader(monthState = MonthState(initialMonth = YearMonth.now()))
    }
}