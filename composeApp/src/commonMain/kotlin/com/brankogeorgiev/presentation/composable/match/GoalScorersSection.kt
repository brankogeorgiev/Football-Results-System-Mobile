package com.brankogeorgiev.presentation.composable.match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.brankogeorgiev.data.GoalEntry
import com.brankogeorgiev.data.GoalEvent
import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.domain.Team

@Composable
fun GoalScorersSection(
    homeTeam: Team,
    awayTeam: Team,
    homePitchPlayers: List<Player>,
    awayPitchPlayers: List<Player>,
    onHomeScoreSync: (Int) -> Unit,
    onAwayScoreSync: (Int) -> Unit,
    onHomeDecrementReady: (() -> Unit) -> Unit,
    onAwayDecrementReady: (() -> Unit) -> Unit,
    onGoalEventsChanged: (homeEvents: List<GoalEvent>, awayEvents: List<GoalEvent>) -> Unit = { _, _ -> }
) {
    var homeGoals by remember { mutableStateOf<List<GoalEntry>>(emptyList()) }
    var awayGoals by remember { mutableStateOf<List<GoalEntry>>(emptyList()) }

    var homeEvents by remember { mutableStateOf<List<GoalEvent>>(emptyList()) }
    var awayEvents by remember { mutableStateOf<List<GoalEvent>>(emptyList()) }

    var pickingForHome by remember { mutableStateOf<Boolean?>(null) }

    fun List<GoalEvent>.toEntries(): List<GoalEntry> =
        groupBy { it.player.id to it.isOwnGoal }
            .map { (_, events) ->
                GoalEntry(
                    player = events.first().player,
                    goals = events.size,
                    isOwnGoal = events.first().isOwnGoal
                )
            }

    fun syncHome() {
        homeGoals = homeEvents.toEntries()
        onHomeScoreSync(homeEvents.size)
        onGoalEventsChanged(homeEvents, awayEvents)
    }

    fun syncAway() {
        awayGoals = awayEvents.toEntries()
        onAwayScoreSync(awayEvents.size)
        onGoalEventsChanged(homeEvents, awayEvents)
    }

    fun removeLastHomeGoal() {
        if (homeEvents.isEmpty()) return
        homeEvents = homeEvents.dropLast(1)
        syncHome()
    }

    fun removeLastAwayGoal() {
        if (awayEvents.isEmpty()) return
        awayEvents = awayEvents.dropLast(1)
        syncAway()
    }

    LaunchedEffect(Unit) {
        onHomeDecrementReady { removeLastHomeGoal() }
        onAwayDecrementReady { removeLastAwayGoal() }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Goal Scorers",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GoalColumn(
                teamName = homeTeam.name,
                goals = homeGoals,
                onAdd = { pickingForHome = true },
                onGoalsChange = { entry, delta ->
                    if (delta > 0) {
                        repeat(delta) {
                            homeEvents = homeEvents + GoalEvent(entry.player, entry.isOwnGoal)
                        }
                    } else {
                        repeat(-delta) {
                            if (homeEvents.isNotEmpty()) {
                                homeEvents = homeEvents.dropLast(1)
                            }
                        }
                    }
                    syncHome()
                },
                modifier = Modifier.weight(1f)
            )

            GoalColumn(
                teamName = awayTeam.name,
                goals = awayGoals,
                onAdd = { pickingForHome = false },
                onGoalsChange = { entry, delta ->
                    if (delta > 0) {
                        repeat(delta) {
                            awayEvents = awayEvents + GoalEvent(entry.player, entry.isOwnGoal)
                        }
                    } else {
                        repeat(-delta) {
                            if (awayEvents.isNotEmpty()) {
                                awayEvents = awayEvents.dropLast(1)
                            }
                        }
                    }
                    syncAway()
                },
                modifier = Modifier.weight(1f)
            )
        }
    }

    pickingForHome?.let { forHome ->
        GoalScorerPickerDialog(
            homeTeamName = homeTeam.name,
            awayTeamName = awayTeam.name,
            homeTeamPlayers = homePitchPlayers,
            awayTeamPlayers = awayPitchPlayers,
            isForHomeTeam = forHome,
            onPlayerSelected = { player, isOwnGoal ->
                if (forHome) {
                    homeEvents = homeEvents + GoalEvent(player, isOwnGoal)
                    syncHome()
                } else {
                    awayEvents = awayEvents + GoalEvent(player, isOwnGoal)
                    syncAway()
                }
                pickingForHome = null
            },
            onDismiss = { pickingForHome = null }
        )
    }
}