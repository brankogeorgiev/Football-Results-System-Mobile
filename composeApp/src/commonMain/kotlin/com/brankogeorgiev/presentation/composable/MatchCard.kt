package com.brankogeorgiev.presentation.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.domain.Team
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber

@Composable
fun MatchCard(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean,
    homeTeam: Team,
    awayTeam: Team,
    homeScore: Int,
    awayScore: Int,
    date: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val showBorder = isPressed || isHovered

    Card(
        modifier = modifier.fillMaxWidth().padding(12.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {}
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            width = 1.dp,
            color = if (showBorder) MaterialTheme.colorScheme.primary
            else Color.LightGray
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (showBorder) 8.dp else 0.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = homeTeam.name,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(12.dp))

                ScoreChip(score = homeScore)
                Text(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    text = ":",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                ScoreChip(score = awayScore)

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = awayTeam.name,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.weight(1f))

                if (isLoggedIn) {
                    ListItemCardActions(onEdit = onEdit, onDelete = onDelete)
                }
            }
            Text(
                text = date,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun String.toFormattedDate(): String {
    return try {
        val date = LocalDate.parse(this)
        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val dayOfWeek = days[date.dayOfWeek.isoDayNumber - 1]
        "$dayOfWeek, ${date.dayOfMonth.toString().padStart(2, '0')}.${
            date.monthNumber.toString().padStart(2, '0')
        }.${date.year}"
    } catch (e: Exception) {
        this
    }
}

@Preview
@Composable
private fun MatchCardPreview() {
    MatchCard(
        Modifier,
        false,
        Team(name = "Beli"),
        Team(name = "Crni"),
        4,
        3,
        "LocalDate(2026, 1, 15)",
        {},
        {})
}
