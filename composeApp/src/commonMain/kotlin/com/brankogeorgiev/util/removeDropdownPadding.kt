package com.brankogeorgiev.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// TODO: Implement this function on every DropdownMenu
fun Modifier.removeDropdownPadding(vertical: Dp = 8.dp): Modifier =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        val newHeight = placeable.height - (vertical * 2).roundToPx()

        layout(placeable.width, newHeight) {
            placeable.placeRelative(0, -vertical.roundToPx())
        }
    }