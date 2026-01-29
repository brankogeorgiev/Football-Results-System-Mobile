package com.brankogeorgiev.presentation.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.brankogeorgiev.util.Resource
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import org.jetbrains.compose.resources.painterResource

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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border =
            if (showBorder) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

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

                ScorePill(score = homeScore)
                Text(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    text = ":",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                ScorePill(score = awayScore)

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = awayTeam.name,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.weight(1f))

                if (isLoggedIn) {
                    MatchActions(onEdit = onEdit, onDelete = onDelete)
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

@Composable
fun MatchActions(onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onEdit) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Resource.Icon.EDIT),
                contentDescription = "",
                tint = Color.Blue,
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Resource.Icon.DELETE),
                contentDescription = "",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun ScorePill(score: Int) {
    Box(
        modifier = Modifier.size(32.dp).background(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(10.dp)
        ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = score.toString(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
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
