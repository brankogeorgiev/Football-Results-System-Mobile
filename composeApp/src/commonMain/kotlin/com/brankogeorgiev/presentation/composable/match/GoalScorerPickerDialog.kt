package com.brankogeorgiev.presentation.composable.match

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.painterResource

@Composable
fun GoalScorerPickerDialog(
    homeTeamName: String,
    awayTeamName: String,
    homeTeamPlayers: List<Player>,
    awayTeamPlayers: List<Player>,
    isForHomeTeam: Boolean,
    onPlayerSelected: (player: Player, isOwnGoal: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var filter by remember { mutableStateOf(if (isForHomeTeam) GoalFilter.HOME else GoalFilter.AWAY) }
    val allPlayers = (homeTeamPlayers + awayTeamPlayers).distinctBy { it.id }
    val filteredPlayers = when (filter) {
        GoalFilter.ALL -> allPlayers
        GoalFilter.HOME -> homeTeamPlayers
        GoalFilter.AWAY -> awayTeamPlayers
    }

    fun isOwnGoal(player: Player): Boolean {
        val isHomePlayer = homeTeamPlayers.any { it.id == player.id }
        val isAwayPlayer = awayTeamPlayers.any { it.id == player.id }
        return if (isForHomeTeam) isAwayPlayer else isHomePlayer
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Goal Scorer",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Text(
                            "✕", fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp)
                ) {
                    listOf(
                        GoalFilter.ALL to "All teams",
                        GoalFilter.HOME to homeTeamName,
                        GoalFilter.AWAY to awayTeamName
                    ).forEach { (gf, label) ->
                        val selected = filter == gf

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                                .clip(RoundedCornerShape(50))
                                .background(if (selected) MaterialTheme.colorScheme.surface else Color.Transparent)
                                .clickable { filter = gf }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().height(320.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredPlayers) { player ->
                        val og = isOwnGoal(player)
                        val bgColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        val textColor = MaterialTheme.colorScheme.onSurface

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(50))
                                .background(bgColor)
                                .clickable { onPlayerSelected(player, og) }
                                .padding(12.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(28.dp).clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Icon(
                                    painter = painterResource(Resource.Icon.PERSON),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = player.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = textColor,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    if (filteredPlayers.isEmpty()) {
                        item {
                            Text(
                                text = "No players assigned to pitch yet",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}