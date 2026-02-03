package com.brankogeorgiev.presentation.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.UserSession
import com.brankogeorgiev.presentation.composable.LoadingIndicator
import com.brankogeorgiev.presentation.composable.MatchCard
import com.brankogeorgiev.presentation.composable.toFormattedDate
import com.brankogeorgiev.util.DisplayResult

@Composable
fun HomeScreen(
    client: ApiClient,
    isLoggedIn: Boolean,
    isAdmin: Boolean,
    userSession: UserSession? = null,
    modifier: Modifier = Modifier
) {
    val viewModel = remember { HomeViewModel(client = client, userSession) }
    val uiState by viewModel.uiState

    uiState.matches.DisplayResult(
        onLoading = { LoadingIndicator() },
        onError = {},
        onSuccess = { matches ->
            Column(modifier = modifier.fillMaxSize().padding(12.dp)) {
                Text(
                    text = "Past Results",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(matches) {
                        val matchDate = it.matchDate.toFormattedDate()
                        MatchCard(
                            isLoggedIn = userSession != null,
                            isAdmin = userSession?.isAdmin ?: false,
                            homeTeam = it.homeTeam,
                            awayTeam = it.awayTeam,
                            homeScore = it.homeScore,
                            awayScore = it.awayScore,
                            date = matchDate,
                            onEdit = {},
                            onDelete = {}
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(ApiClient(), true, false, UserSession("", "", true))
    }
}