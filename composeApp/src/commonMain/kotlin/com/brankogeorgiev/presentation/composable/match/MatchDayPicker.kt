package com.brankogeorgiev.presentation.composable.match

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.brankogeorgiev.util.Resource
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.painterResource

private fun ordinal(day: Int): String = when {
    day in 11..13 -> "${day}th"
    day % 10 == 1 -> "${day}st"
    day % 10 == 2 -> "${day}nd"
    day % 10 == 3 -> "${day}rd"
    else -> "${day}th"
}

fun Month.displayName(): String = when (this) {
    Month.JANUARY -> "January"
    Month.FEBRUARY -> "February"
    Month.MARCH -> "March"
    Month.APRIL -> "April"
    Month.MAY -> "May"
    Month.JUNE -> "June"
    Month.JULY -> "July"
    Month.AUGUST -> "August"
    Month.SEPTEMBER -> "September"
    Month.OCTOBER -> "October"
    Month.NOVEMBER -> "November"
    Month.DECEMBER -> "December"
}

fun LocalDate.formatted(): String = "${month.displayName()} ${ordinal(dayOfMonth)}, $year"

fun daysInMonth(year: Int, month: Month): Int {
    val first = LocalDate(year, month, 1)
    val next = first.plus(1, DateTimeUnit.MONTH)
    return (next.toEpochDays() - first.toEpochDays()).toInt()
}

fun firstDayOfWeekIndex(year: Int, month: Month): Int {
    val dow = LocalDate(year, month, 1).dayOfWeek
    return when (dow) {
        DayOfWeek.SUNDAY -> 0
        DayOfWeek.MONDAY -> 1
        DayOfWeek.TUESDAY -> 2
        DayOfWeek.WEDNESDAY -> 3
        DayOfWeek.THURSDAY -> 4
        DayOfWeek.FRIDAY -> 5
        DayOfWeek.SATURDAY -> 6
        else -> 0
    }
}

@Composable
fun MatchDatePicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCalendar by remember { mutableStateOf(false) }
    var calendarYear by remember { mutableStateOf(selectedDate.year) }
    var calendarMonth by remember { mutableStateOf(selectedDate.month) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Match Date",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        calendarYear = selectedDate.year
                        calendarMonth = selectedDate.month
                        showCalendar = true
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(Resource.Icon.CALENDAR),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = selectedDate.formatted(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (showCalendar) {
                Popup(
                    alignment = Alignment.TopStart,
                    onDismissRequest = { showCalendar = false },
                    properties = PopupProperties(focusable = true)
                ) {
                    CalendarCard(
                        selectedDate = selectedDate,
                        calendarYear = calendarYear,
                        calendarMonth = calendarMonth,
                        onMonthChange = { y, m ->
                            calendarYear = y
                            calendarMonth = m
                        },
                        onDaySelected = { date ->
                            onDateSelected(date)
                            showCalendar = false
                        }
                    )
                }
            }
        }
    }
}