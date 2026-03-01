package com.brankogeorgiev.presentation.screen.players

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
import androidx.compose.ui.unit.dp
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.UserSession
import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.data.repository.AdminRepository
import com.brankogeorgiev.presentation.AddPlayerDialog
import com.brankogeorgiev.presentation.DeleteConfirmationDialog
import com.brankogeorgiev.presentation.composable.LoadingIndicator
import com.brankogeorgiev.presentation.composable.PlayerCard
import com.brankogeorgiev.util.DisplayResult
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlayersScreen(
    client: ApiClient,
    isLoggedIn: Boolean,
    isAdmin: Boolean,
    userSession: UserSession? = null,
    adminRepository: AdminRepository,
    modifier: Modifier = Modifier
) {
    val viewModel = remember { PlayersViewModel(client, userSession, adminRepository) }
    val uiState by viewModel.uiState

    var showDialog by remember { mutableStateOf(false) }
    var editingPlayer by remember { mutableStateOf<Player?>(null) }
    var deletingPlayer by remember { mutableStateOf<Player?>(null) }

    uiState.players.DisplayResult(
        onLoading = { LoadingIndicator() },
        onError = {},
        onSuccess = { players ->
            Column(modifier = modifier.fillMaxSize().padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Players",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )

                    if (isAdmin) {
                        Button(onClick = { showDialog = true }, shape = RoundedCornerShape(12.dp)) {
                            Icon(
                                painter = painterResource(Resource.Icon.ADD),
                                contentDescription = "Add Player"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Add Player")
                        }
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(players) { player ->
                        PlayerCard(
                            player = player,
                            isAdmin = isAdmin,
                            isLoggedIn = isLoggedIn,
                            onEdit = { editingPlayer = player },
                            onDelete = { deletingPlayer = player }
                        )
                    }
                }
            }

            if (showDialog) {
                AddPlayerDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { name, teamId ->
                        viewModel.addPlayer(name = name, teamId = teamId)
                        showDialog = false
                    }
                )
            }

            editingPlayer?.let { player ->
                AddPlayerDialog(
                    initialName = player.name,
                    initialTeamId = player.team?.id ?: "",
                    isEdit = true,
                    onDismiss = { editingPlayer = null },
                    onConfirm = { name, teamId ->
                        viewModel.updatePlayer(
                            id = player.id,
                            name = name,
                            teamId = teamId
                        )
                        editingPlayer = null
                    }
                )
            }

            deletingPlayer?.let { player ->
                DeleteConfirmationDialog(
                    onDismiss = { deletingPlayer = null },
                    onConfirm = {
                        viewModel.deletePlayer(player.id)
                        deletingPlayer = null
                    }
                )
            }
        }
    )
}
