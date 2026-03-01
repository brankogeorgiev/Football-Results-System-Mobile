package com.brankogeorgiev.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.util.removeDropdownPadding


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlayerDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String?) -> Unit,
    initialName: String = "",
    initialTeamId: String? = null,
    isEdit: Boolean = false
) {
    var playerName by remember { mutableStateOf(initialName) }
    val teams = listOf(
        null to "No team",
        "aa9ea52f-d297-4c6a-9225-39c7a8d3a24f" to "Beli",
        "da6e4857-13f7-4ec0-8ad4-983153b5d3e0" to "Crni"
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedTeam by remember { mutableStateOf<String?>(initialTeamId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text(
                text = if (!isEdit) "Add Player" else "Update Player",
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
                        onConfirm(playerName, selectedTeam)
                    },
                    enabled = playerName.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) { Text(text = "Add Player") }
            }
        }
    )
}