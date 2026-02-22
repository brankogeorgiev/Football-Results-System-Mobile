package com.brankogeorgiev.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.domain.TeamStanding
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.painterResource

@Composable
fun TeamStandingCard(standings: List<TeamStanding>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(Resource.Icon.TROPHY),
                    contentDescription = null,
                    tint = Color(0xFF10B981)
                )

                Text(
                    text = "Team Standings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            standings.forEachIndexed { index, standing ->
                TeamStandingRow(position = index + 1, standing = standing)

                if (index != standings.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color(0xFFE5E7EB)
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamStandingRow(
    position: Int,
    standing: TeamStanding
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = position.toString(),
            modifier = Modifier.width(28.dp).padding(end = 4.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6B7280)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = standing.team,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "${standing.played} matches",
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatValue(value = standing.wins, color = Color(0xFF16A34A))
                StatValue(value = standing.draws, color = Color(0xFF6B7280))
                StatValue(value = standing.losses, color = Color(0xFFDC2626))
                StatValue(
                    value = "${standing.goalsFor}-${standing.goalsAgainst}",
                    color = Color.Black,
                    width = 80.dp
                )
                StatValue(
                    value = standing.points,
                    color = Color(0xFF10B981),
                    bold = true,
                    fontSize = 18.sp,
                    width = 40.dp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                StatLabel(text = "W")
                StatLabel(text = "D")
                StatLabel(text = "L")
                StatLabel(text = "GD", width = 80.dp)
                StatLabel(text = "Pts", width = 40.dp)
            }
        }
    }
}

@Composable
private fun StatValue(
    value: Any,
    color: Color,
    bold: Boolean = false,
    width: Dp = 28.dp,
    fontSize: TextUnit = 16.sp
) {
    Text(
        text = value.toString(),
        modifier = Modifier.width(width),
        textAlign = TextAlign.Center,
        fontSize = fontSize,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
        color = color
    )
}

@Composable
private fun StatLabel(text: String, width: Dp = 28.dp) {
    Text(
        text = text,
        modifier = Modifier.width(width),
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF6B7280)
    )
}