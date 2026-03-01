package com.brankogeorgiev.presentation.screen.players

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.UserSession
import com.brankogeorgiev.data.repository.AdminRepository
import com.brankogeorgiev.presentation.composable.LoadingIndicator
import com.brankogeorgiev.presentation.composable.PlayerCard
import com.brankogeorgiev.util.DisplayResult
import com.brankogeorgiev.util.Resource
import com.brankogeorgiev.util.removeDropdownPadding
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

            if (showDialog) {
                AddPlayerDialog(
                    onDismiss = { showDialog = false },
                    onAdd = { name, teamId ->
                        viewModel.addPlayer(name = name, teamId = teamId)
                        showDialog = false
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlayerDialog(onDismiss: () -> Unit, onAdd: (String, String?) -> Unit) {
    var playerName by remember { mutableStateOf("") }
    val teams = listOf(
        null to "No team",
        "aa9ea52f-d297-4c6a-9225-39c7a8d3a24f" to "Beli",
        "da6e4857-13f7-4ec0-8ad4-983153b5d3e0" to "Crni"
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedTeam by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text(
                text = "Add Player",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Player Name",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(4.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it },
                    placeholder = {
                        Text(
                            text = "Enter player name",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Default Team",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(4.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = teams.firstOrNull { it.first == selectedTeam }?.second
                            ?: "No team",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        modifier = Modifier.removeDropdownPadding(8.dp),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        teams.forEach { (teamId, teamName) ->
                            val isSelected = selectedTeam == teamId

                            DropdownMenuItem(
                                text = { Text(text = teamName) },
                                onClick = {
                                    selectedTeam = teamId
                                    expanded = false
                                },
                                modifier = Modifier.fillMaxWidth().background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onAdd(playerName, selectedTeam)
                    },
                    enabled = playerName.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) { Text(text = "Add Player") }
            }
        }
    )
}
