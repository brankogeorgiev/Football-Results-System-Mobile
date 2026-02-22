package com.brankogeorgiev.presentation.screen.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.presentation.composable.FiltersCard
import com.brankogeorgiev.presentation.composable.LoadingIndicator
import com.brankogeorgiev.presentation.composable.ScorersListCard
import com.brankogeorgiev.presentation.composable.TeamStandingCard
import com.brankogeorgiev.util.RequestState
import com.brankogeorgiev.util.calculateGoalScorers
import com.brankogeorgiev.util.calculateStandings

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun StatsScreen(
    client: ApiClient,
    modifier: Modifier = Modifier
) {
    val viewModel = remember { StatsViewModel(client = client) }
    val uiState = viewModel.uiState

    var filtersExpanded by remember { mutableStateOf(false) }

    when (val state = uiState.value.data) {
        is RequestState.Loading -> {
            LoadingIndicator()
        }

        is RequestState.Error -> {
            Text(text = "Error loading stats")
        }

        is RequestState.Success -> {
            val data = state.data
            val matches = data.matches

            val allTeams = remember(matches) {
                matches.flatMap { listOf(it.homeTeam.name, it.awayTeam.name) }.distinct().sorted()
            }

            val standings by remember(
                matches,
                data.dateFrom,
                data.dateTo,
                data.team1Filter,
                data.team2Filter
            ) {
                derivedStateOf {
                    calculateStandings(
                        matches = matches,
                        dateFrom = data.dateFrom,
                        dateTo = data.dateTo,
                        team1Filter = data.team1Filter,
                        team2Filter = data.team2Filter
                    )
                }
            }

            val topScorers by remember(
                data.goals,
                data.matches,
                data.dateFrom,
                data.dateTo,
                data.team1Filter,
                data.team2Filter
            ) {
                derivedStateOf {
                    calculateGoalScorers(data = data, isOwnGoal = false)
                }
            }

            val ownGoals by remember(
                data.goals,
                data.matches,
                data.dateFrom,
                data.dateTo,
                data.team1Filter,
                data.team2Filter
            ) {
                derivedStateOf {
                    calculateGoalScorers(data = data, isOwnGoal = true)
                }
            }

            LazyColumn(
                modifier = modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Statistics",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                item {
                    FiltersCard(
                        expanded = filtersExpanded,
                        onExpandChange = { filtersExpanded = it },
                        dateFrom = data.dateFrom,
                        dateTo = data.dateTo,
                        team1Filter = data.team1Filter,
                        team2Filter = data.team2Filter,
                        allTeams = allTeams,
                        onDateFromChange = viewModel::updateDateFrom,
                        onDateToChange = viewModel::updateDateTo,
                        onTeam1FilterChange = viewModel::updateTeam1Filter,
                        onTeam2FilterChange = viewModel::updateTeam2Filter,
                        onClearFilters = viewModel::clearFilters
                    )
                }

                item { TeamStandingCard(standings = standings) }

                item {
                    ScorersListCard(
                        scorers = topScorers,
                        title = "Top Scorers",
                        color = Color(0xFF10B981),
                        emptyListText = "No goal scorers."
                    )
                }

                item {
                    ScorersListCard(
                        scorers = ownGoals,
                        title = "Own Goals",
                        color = Color.Red,
                        emptyListText = "No own goals.",
                        ownGoal = true
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun StatsScreenPreview() {
    StatsScreen(client = ApiClient())
}