package com.example.trackit.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackit.ui.theme.TrackItTheme
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.SelectableWeekCalendar

@Composable
fun ExpandableCalendar(){
    var expanded by remember { mutableStateOf(false) }

    Card(elevation = 8.dp) {
        Column(modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExpandButton(expanded, onExpandedChanged = {expanded = it})

            if (!expanded){
                SelectableWeekCalendar(
                    weekHeader = {}
                )
            }
            else {
                SelectableCalendar()
            }
        }
    }
}

@Composable
private fun ExpandButton(expanded: Boolean, onExpandedChanged: (Boolean) -> Unit, ){
    Button(
        onClick = { onExpandedChanged(!expanded) }
    ) {
        Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarPreview(){
    TrackItTheme {
        ExpandableCalendar()
    }
}