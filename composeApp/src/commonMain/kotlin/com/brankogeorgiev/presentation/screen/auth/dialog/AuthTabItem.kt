package com.brankogeorgiev.presentation.screen.auth.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AuthTabItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: DrawableResource,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Color.White else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Icon(
                painter = painterResource(icon),
                contentDescription = "",
                tint = if (selected) Color.Black else Color.Gray
            )
            Text(
                text = text,
                color = if (selected) Color.Black else Color.Gray,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}