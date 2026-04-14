package com.brankogeorgiev.presentation.screen.add_result

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.brankogeorgiev.data.GoalEvent
import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.domain.Team
import com.brankogeorgiev.presentation.composable.match.FootballPitchSelection
import com.brankogeorgiev.presentation.composable.match.GoalScorersSection
import com.brankogeorgiev.presentation.composable.match.MatchDatePicker
import com.brankogeorgiev.presentation.composable.match.TeamScoreSelector
import com.brankogeorgiev.util.RequestState
import com.brankogeorgiev.util.Resource
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddResultDialog(
    uiState: AddResultUiState,
    onHomeTeamSelected: (Team) -> Unit,
    onAwayTeamSelected: (Team) -> Unit,
    onHomeScoreChange: (Int) -> Unit,
    onAwayScoreChange: (Int) -> Unit,
    onPlayerAdded: (name: String, teamId: String?) -> Unit,
    onSlotsChanged: (homePlayers: List<Player>, awayPlayers: List<Player>) -> Unit,
    onGoalEventsChanged: (homeEvents: List<GoalEvent>, awayEvents: List<GoalEvent>) -> Unit,
    onMatchDateChange: (LocalDate) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    var removeLastHomeGoal by remember { mutableStateOf({}) }
    var removeLastAwayGoal by remember { mutableStateOf({}) }

    LaunchedEffect(uiState.saveResult) {
        if (uiState.saveResult is RequestState.Success) onDismiss()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(40.dp))
                    Text(
                        text = "Add result",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            painter = painterResource(Resource.Icon.CLOSE),
                            contentDescription = "Close"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (val teamsState = uiState.teams) {
                    is RequestState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is RequestState.Error -> {
                        Text(
                            text = "Failed to load teams: ${teamsState.message}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    is RequestState.Success -> {
                        val teams = teamsState.data
                        val homeTeam = uiState.homeTeam ?: return@Card
                        val awayTeam = uiState.awayTeam ?: return@Card

                        val homeOptions = teams.filter { it.id != awayTeam.id }.map { it.name }
                        val awayOptions = teams.filter { it.id != homeTeam.id }.map { it.name }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            TeamScoreSelector(
                                teams = homeOptions,
                                selectedTeam = homeTeam.name,
                                score = uiState.homeScore,
                                onTeamSelected = { name ->
                                    teams.firstOrNull { it.name == name }
                                        ?.let { onHomeTeamSelected(it) }
                                },
                                onScoreChange = onHomeScoreChange,
                                removeLastGoal = removeLastHomeGoal
                            )
                            TeamScoreSelector(
                                teams = awayOptions,
                                selectedTeam = awayTeam.name,
                                score = uiState.awayScore,
                                onTeamSelected = { name ->
                                    teams.firstOrNull { it.name == name }
                                        ?.let { onAwayTeamSelected(it) }
                                },
                                onScoreChange = onAwayScoreChange,
                                removeLastGoal = removeLastAwayGoal
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(16.dp))

                        val players = when (val p = uiState.players) {
                            is RequestState.Success -> p.data
                            else -> emptyList()
                        }

                        FootballPitchSelection(
                            homeTeamName = homeTeam.name,
                            awayTeamName = awayTeam.name,
                            homeTeamId = homeTeam.id,
                            awayTeamId = awayTeam.id,
                            availablePlayers = players,
                            onPlayerAdded = onPlayerAdded,
                            onSlotsChanged = onSlotsChanged,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(420.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(16.dp))

                        GoalScorersSection(
                            homeTeam = homeTeam,
                            awayTeam = awayTeam,
                            homePitchPlayers = uiState.homePlayers,
                            awayPitchPlayers = uiState.awayPlayers,
                            onHomeScoreSync = onHomeScoreChange,
                            onAwayScoreSync = onAwayScoreChange,
                            onHomeDecrementReady = { removeLastHomeGoal = it },
                            onAwayDecrementReady = { removeLastAwayGoal = it },
                            onGoalEventsChanged = onGoalEventsChanged
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(16.dp))

                        MatchDatePicker(
                            selectedDate = uiState.matchDate,
                            onDateSelected = onMatchDateChange
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        val isSaving = uiState.saveResult is RequestState.Loading

                        Button(
                            onClick = onSave,
                            enabled = !isSaving,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(text = if (isSaving) "Saving…" else "Save result")
                        }

                        if (uiState.saveResult is RequestState.Error) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = (uiState.saveResult as RequestState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
    }
}