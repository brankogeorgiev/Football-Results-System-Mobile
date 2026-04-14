package com.brankogeorgiev.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.UserSession
import com.brankogeorgiev.data.repository.AdminRepository
import com.brankogeorgiev.data.repository.AuthRepository
import com.brankogeorgiev.presentation.composable.LoadingIndicator
import com.brankogeorgiev.presentation.composable.MatchCard
import com.brankogeorgiev.presentation.composable.toFormattedDate
import com.brankogeorgiev.presentation.screen.add_result.AddResultDialog
import com.brankogeorgiev.presentation.screen.add_result.AddResultViewModel
import com.brankogeorgiev.util.DisplayResult
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(
    client: ApiClient,
    isLoggedIn: Boolean,
    isAdmin: Boolean,
    userSession: UserSession? = null,
    adminRepository: AdminRepository,
    modifier: Modifier = Modifier
) {
    val viewModel = remember { HomeViewModel(client = client, userSession = userSession) }
    val uiState by viewModel.uiState
    var showAddResultDialog by remember { mutableStateOf(false) }

    val addResultViewModel = remember {
        AddResultViewModel(
            client = client,
            session = userSession,
            adminRepository = adminRepository
        )
    }
    val addResultUiState = addResultViewModel.uiState.value

    uiState.matches.DisplayResult(
        onLoading = { LoadingIndicator() },
        onError = {},
        onSuccess = { matches ->
            Column(modifier = modifier.fillMaxSize().padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Past Results",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )

                    if (isLoggedIn && isAdmin) {
                        Button(
                            onClick = { showAddResultDialog = true },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                painter = painterResource(Resource.Icon.ADD),
                                contentDescription = "Add Result"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Add Result")
                        }
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(matches) { match ->
                        val matchDate = match.matchDate.toFormattedDate()
                        MatchCard(
                            isLoggedIn = userSession != null,
                            isAdmin = userSession?.isAdmin ?: false,
                            homeTeam = match.homeTeam,
                            awayTeam = match.awayTeam,
                            homeScore = match.homeScore,
                            awayScore = match.awayScore,
                            date = matchDate,
                            onEdit = {},
                            onDelete = {}
                        )
                    }
                }
            }

            if (showAddResultDialog) {
                AddResultDialog(
                    uiState = addResultUiState,
                    onHomeTeamSelected = addResultViewModel::setHomeTeam,
                    onAwayTeamSelected = addResultViewModel::setAwayTeam,
                    onHomeScoreChange = addResultViewModel::setHomeScore,
                    onAwayScoreChange = addResultViewModel::setAwayScore,
                    onPlayerAdded = { name, teamId ->
                        if (teamId != null) addResultViewModel.addPlayer(name, teamId)
                    },
                    onSlotsChanged = addResultViewModel::onPitchSlotsChanged,
                    onGoalEventsChanged = addResultViewModel::onGoalEventChanged,
                    onSave = addResultViewModel::saveResult,
                    onDismiss = { showAddResultDialog = false },
                    onMatchDateChange = addResultViewModel::setMatchDate,
                )
            }
        }
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            client = ApiClient(),
            isLoggedIn = true,
            isAdmin = false,
            userSession = UserSession("", "", "", "", true),
            adminRepository = AdminRepository(ApiClient(), AuthRepository(ApiClient()))
        )
    }
}