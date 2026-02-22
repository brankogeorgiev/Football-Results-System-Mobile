package com.brankogeorgiev.util

import androidx.compose.material3.SelectableDates

class IsSelectableDate(
    private val startDateMillis: Long?,
    private val endDateMillis: Long?
) : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val after = startDateMillis?.let { utcTimeMillis >= it } ?: true
        val before = endDateMillis?.let { utcTimeMillis <= it } ?: true
        return after && before
    }
}