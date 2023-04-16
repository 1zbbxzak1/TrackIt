package com.example.trackit.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackit.R
import com.example.trackit.ui.theme.TrackItTheme
import io.github.boguszpawlowski.composecalendar.header.MonthState
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun MonthHeader(
    monthState: MonthState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Row(
            modifier = modifier.clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = monthState.currentMonth.month
                        .getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
                        .lowercase()
                        .replaceFirstChar { it.titlecase() },
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 3.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = monthState.currentMonth.year.toString(),
                    style = MaterialTheme.typography.h6
                )
            }

            Box(modifier = Modifier.height(14.dp), contentAlignment = Alignment.Center){
                Icon(painterResource(id = R.drawable.expand_icon), contentDescription = null)
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}