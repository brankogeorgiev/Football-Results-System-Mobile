package com.brankogeorgiev.presentation.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brankogeorgiev.presentation.composable.MatchCard
import com.brankogeorgiev.presentation.composable.toFormattedDate
import com.brankogeorgiev.util.DisplayResult

@Composable
fun HomeScreen(
    uiState: HomeUIState,
    isLoggedIn: Boolean,
    isAdmin: Boolean,
    modifier: Modifier = Modifier
) {

    uiState.matches.DisplayResult(
        onLoading = {
            CircularProgressIndicator()
        },
        onError = {},
        onSuccess = { matches ->
            LazyColumn(
                modifier = modifier.padding(12.dp).fillMaxSize()
            ) {
                items(matches) {
                    val matchDate = it.matchDate.toFormattedDate()
                    MatchCard(
                        isLoggedIn = isLoggedIn,
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
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(HomeUIState(), true, false)
    }
}