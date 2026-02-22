package com.brankogeorgiev.presentation.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.domain.TopScorer
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.painterResource


@Composable
fun ScorersListCard(
    scorers: List<TopScorer>,
    title: String,
    color: Color,
    ownGoal: Boolean = false,
    emptyListText: String
) {
    var showAll by remember { mutableStateOf(false) }
    val topThree = scorers.take(3)
    val remaining = scorers.drop(3)

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        border = if (ownGoal) BorderStroke(width = 1.dp, color = color) else null
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(all = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(Resource.Icon.GOAL),
                    contentDescription = "",
                    tint = color
                )
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (scorers.isEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(all = 12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = emptyListText, color = Color.Gray)
                }
            } else {
                topThree.forEachIndexed { index, scorer ->
                    ScorerRow(rank = index + 1, scorer = scorer, ownGoal = ownGoal)

                    if (index != topThree.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 6.dp),
                            color = Color(0xFFE5E7EB)
                        )
                    }
                }

                if (showAll) {
                    remaining.forEachIndexed { index, scorer ->
                        if (index == 0) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 6.dp),
                                color = Color(0xFFE5E7EB)
                            )
                        }

                        ScorerRow(rank = index + 4, scorer = scorer, ownGoal = ownGoal)

                        if (index != remaining.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 6.dp),
                                color = Color(0xFFE5E7EB)
                            )
                        }
                    }
                }

                if (remaining.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = color),
                        onClick = { showAll = !showAll }
                    ) {
                        Text(text = if (showAll) "Show less" else "Show more")
                    }
                }
            }
        }
    }
}

@Composable
fun ScorerRow(
    rank: Int, scorer: TopScorer,
    ownGoal: Boolean = false
) {
    val indexColor = if (!ownGoal) {
        when (rank) {
            1 -> Color(0xFFFFD700)
            2 -> Color(0xFFC0C0C0)
            3 -> Color(0xFFCD7F32)
            else -> Color.Gray
        }
    } else Color.Red

    val goalNumberColor = if (!ownGoal) Color(0xFF10B981) else Color.Red

    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "$rank. ",
                fontSize = 20.sp,
                color = indexColor,
                fontWeight = FontWeight.Bold
            )
            Text(text = scorer.name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = scorer.goals.toString(),
                fontSize = 18.sp,
                color = goalNumberColor,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = if (ownGoal) " OG" else " ${if (scorer.goals == 1) "goal" else "goals"}",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}