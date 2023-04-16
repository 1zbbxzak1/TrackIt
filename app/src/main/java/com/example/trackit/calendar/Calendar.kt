package com.example.trackit.calendar

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackit.ui.theme.TrackItTheme
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.SelectableWeekCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.rememberSelectableWeekCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import io.github.boguszpawlowski.composecalendar.week.Week
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun ExpandableCalendar(
    expanded: Boolean,
    onClick: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    currentDate: LocalDate = LocalDate.now(),
    modifier: Modifier = Modifier
){
    val selectionState = remember {
        DynamicSelectionState(selection = listOf(currentDate), selectionMode = SelectionMode.Single)
    }

    Column(
        modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    ) {
        if (!expanded){
            val calendarState = rememberSelectableWeekCalendarState(
                selectionState = selectionState,
                initialWeek = getWeekFromDate(selectionState.selection)
            )

            CalendarCard(modifier.clickable { onClick() }
                .padding(start = 8.dp, end = 8.dp, bottom = 10.dp),
                cardContent = {
                    SelectableWeekCalendar(
                        calendarState = calendarState,
                        dayContent = {DayContent(state = it)},
                        weekHeader = {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onClick() }
                            ) {
                                ExpandIcon(expanded = false)
                            }
                        }
                    )
                }
            )

            if (calendarState.selectionState.selection.isNotEmpty()){
                onDateSelected(calendarState.selectionState.selection[0])
            } else {
                onDateSelected(LocalDate.now())
                calendarState.selectionState.onDateSelected(LocalDate.now())
            }
        }
        else {
            val calendarState = rememberSelectableCalendarState(
                selectionState = selectionState,
                initialMonth = getMonthFromDate(selectionState.selection)
            )

            CalendarCard(modifier.clickable { onClick() }
                .padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
                cardContent = {
                    SelectableCalendar(
                        calendarState = calendarState,
                        dayContent = {DayContent(state = it)},
                        monthHeader = { MonthHeader(monthState = it, onClick) }
                    )
                }
            )

            if (calendarState.selectionState.selection.isNotEmpty()){
                onDateSelected(calendarState.selectionState.selection[0])
            } else{
                onDateSelected(LocalDate.now())
                calendarState.selectionState.onDateSelected(LocalDate.now())
            }
        }
    }
}

@Composable
private fun CalendarCard(
    modifier: Modifier = Modifier,
    cardContent: @Composable () -> Unit
    ){
    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            cardContent()
        }
    }
}

@Composable
private fun DayContent(
    state: DayState<DynamicSelectionState>,
    modifier: Modifier = Modifier,
    selectionColor: Color = MaterialTheme.colors.secondary,
    currentDayColor: Color = MaterialTheme.colors.primary,
    onClick: (LocalDate) -> Unit = {},
){
    val date = state.date
    val selectionState = state.selectionState

    val isSelected = selectionState.isDateSelected(date)

    val border = if (isSelected) BorderStroke(1.dp, currentDayColor) else null

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp),
        elevation = 0.dp,
        border = border
    ) {
        Box(
            modifier = Modifier.clickable {
                    onClick(date)
                    selectionState.onDateSelected(date)
                }
                .background(
                    if (state.isCurrentDay && isSelected) Color.Yellow
                    else MaterialTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                Modifier
                    .alpha(if (state.isFromCurrentMonth) 1.0f else 0.6f)
                    .background(if (state.isCurrentDay) Color.Yellow
                    else MaterialTheme.colors.surface)
                    .padding(2.dp)
            )
        }
    }
}

@Composable
fun ExpandIcon(expanded: Boolean){
    val icon = if(!expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown
    Icon(icon, contentDescription = null)
}

private fun getMonthFromDate(dateList: List<LocalDate>): YearMonth {
    if (dateList.isNotEmpty()){
        val date = dateList[0]
        return YearMonth.from(date)

    }
    return YearMonth.now()
}

private fun getWeekFromDate(dateList: List<LocalDate>): Week {
    if (dateList.isNotEmpty()){
        val date = dateList[0]
        val monday = date.minusDays((date.dayOfWeek.value - 1).toLong())
        val weekList = List<LocalDate> (7) { monday.plusDays(it.toLong()) }

        return Week(weekList)
    }
    return Week.now()
}

@Preview(showBackground = true)
@Composable
private fun CalendarPreview(){
    TrackItTheme {
        ExpandableCalendar(expanded = true, onClick = { /*TODO*/ }, onDateSelected = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarWeekPreview(){
    TrackItTheme {
        ExpandableCalendar(expanded = false, onClick = { /*TODO*/ }, onDateSelected = {})
    }
}