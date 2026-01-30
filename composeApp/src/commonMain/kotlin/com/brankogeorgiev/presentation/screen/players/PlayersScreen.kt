package com.brankogeorgiev.presentation.screen.players

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
import androidx.compose.ui.unit.dp
import com.brankogeorgiev.data.network.ApiClient
import com.brankogeorgiev.presentation.composable.LoadingIndicator
import com.brankogeorgiev.presentation.composable.PlayerCard
import com.brankogeorgiev.util.DisplayResult

@Composable
fun PlayersScreen(
    client: ApiClient,
    isLoggedIn: Boolean,
    isAdmin: Boolean,
    modifier: Modifier = Modifier
) {
    val viewModel = remember { PlayersViewModel(client) }
    val uiState by viewModel.uiState

    uiState.players.DisplayResult(
        onLoading = { LoadingIndicator() },
        onError = {},
        onSuccess = { players ->
            Column(modifier = modifier.fillMaxSize().padding(12.dp)) {
                Text(
                    text = "Players",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(players) {
                        PlayerCard(
                            player = it,
                            isAdmin = isAdmin,
                            isLoggedIn = isLoggedIn,
                            onEdit = {},
                            onDelete = {}
                        )
                    }
                }
            }
        }
    )
}
