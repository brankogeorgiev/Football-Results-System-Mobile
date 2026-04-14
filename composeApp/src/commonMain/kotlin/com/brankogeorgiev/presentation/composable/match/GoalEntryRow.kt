package com.brankogeorgiev.presentation.composable.match

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.data.GoalEntry
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.painterResource

@Composable
fun GoalEntryRow(
    entry: GoalEntry,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    val isOg = entry.isOwnGoal
    val bgColor = if (isOg) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    val textColor = if (isOg) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.onSurface

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .then(
                if (isOg) {
                    Modifier.border(
                        2.dp,
                        MaterialTheme.colorScheme.error,
                        RoundedCornerShape(12.dp)
                    )
                } else Modifier
            )
            .background(bgColor)
            .padding(start = 10.dp, end = 4.dp, top = 6.dp, bottom = 6.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    if (isOg) MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant
                )
        ) {
            Icon(
                painter = painterResource(Resource.Icon.PERSON),
                contentDescription = "",
                tint = if (isOg) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = if (isOg) "${entry.player.name} (OG)" else entry.player.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMinus, modifier = Modifier.size(28.dp)) {
                Text("−", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
            Text(
                text = entry.goals.toString(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            IconButton(onClick = onPlus, modifier = Modifier.size(28.dp)) {
                Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
        }
    }
}