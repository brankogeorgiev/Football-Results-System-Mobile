package com.brankogeorgiev.presentation.composable.match

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus

@Composable
fun CalendarCard(
    selectedDate: LocalDate,
    calendarYear: Int,
    calendarMonth: Month,
    onMonthChange: (Int, Month) -> Unit,
    onDaySelected: (LocalDate) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.width(300.dp).padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val prev = LocalDate(calendarYear, calendarMonth, 1)
                            .plus(-1, DateTimeUnit.MONTH)
                        onMonthChange(prev.year, prev.month)
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Text(
                        "<", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "${calendarMonth.displayName()} $calendarYear",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = {
                        val next = LocalDate(calendarYear, calendarMonth, 1)
                            .plus(1, DateTimeUnit.MONTH)
                        onMonthChange(next.year, next.month)
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Text(
                        ">", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val dayHeaders = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
            Row(modifier = Modifier.fillMaxWidth()) {
                dayHeaders.forEach { label ->
                    Text(
                        text = label,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            val totalDays = daysInMonth(calendarYear, calendarMonth)
            val firstDowIndex = firstDayOfWeekIndex(calendarYear, calendarMonth)

            val prevMonthDate = LocalDate(calendarYear, calendarMonth, 1)
                .plus(-1, DateTimeUnit.MONTH)
            val prevMonthDays = daysInMonth(prevMonthDate.year, prevMonthDate.month)

            val cells = mutableListOf<Int>()
            for (i in firstDowIndex downTo 1) cells.add(-(prevMonthDays - i + 1))
            for (d in 1..totalDays) cells.add(d)
            while (cells.size % 7 != 0) cells.add(-(cells.size))

            cells.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { cell ->
                        val isCurrentMonth = cell > 0
                        val day = if (isCurrentMonth) cell else -cell
                        val date = if (isCurrentMonth)
                            LocalDate(calendarYear, calendarMonth, day)
                        else null
                        val isSelected = date != null && date == selectedDate

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.weight(1f).aspectRatio(1f)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSelected -> MaterialTheme.colorScheme.primary
                                        else -> Color.Transparent
                                    }
                                )
                                .then(
                                    if (isCurrentMonth && date != null)
                                        Modifier.clickable { onDaySelected(date) } else Modifier)
                        ) {
                            Text(
                                text = day.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = when {
                                    isSelected -> MaterialTheme.colorScheme.onPrimary
                                    isCurrentMonth -> MaterialTheme.colorScheme.onSurface
                                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                },
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}