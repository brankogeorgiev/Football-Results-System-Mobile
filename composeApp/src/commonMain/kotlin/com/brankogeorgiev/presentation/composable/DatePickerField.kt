package com.brankogeorgiev.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.util.IsSelectableDate
import com.brankogeorgiev.util.Resource
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun DatePickerField(
    date: String,
    text: String,
    onDateChange: (String) -> Unit,
    startDateMillis: Long? = null,
    endDateMillis: Long? = null,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val initialMillis = remember(date) {
        if (date.isNotEmpty()) {
            try {
                val parts = date.split(".")
                val day = parts[0].toInt()
                val month = parts[1].toInt()
                val year = parts[2].toInt()

                LocalDate(year, month, day)
                    .atStartOfDayIn(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
            } catch (e: Exception) {
                Clock.System.now().toEpochMilliseconds()
            }
        } else {
            Clock.System.now().toEpochMilliseconds()
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis,
        selectableDates = IsSelectableDate(
            startDateMillis = startDateMillis,
            endDateMillis = endDateMillis
        )
    )

    Column(modifier = modifier) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = date,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(text = text, fontSize = 14.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            trailingIcon = {
                Icon(
                    painter = painterResource(Resource.Icon.CALENDAR),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        showDatePicker = true
                    }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedBorderColor = Color(0xFF10B981)
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->

                                val localDate = Instant
                                    .fromEpochMilliseconds(millis)
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                                    .date

                                val formattedDate =
                                    "${localDate.dayOfMonth.toString().padStart(2, '0')}." +
                                            "${
                                                localDate.monthNumber.toString().padStart(2, '0')
                                            }." +
                                            localDate.year

                                onDateChange(formattedDate)
                            }

                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
