package com.brankogeorgiev.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TeamPickerField(
    teamFilter: String,
    team: String,
    allTeams: List<String>,
    onTeamFilterChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = team,
            fontSize = 12.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        DropdownMenuField(
            value = teamFilter,
            items = listOf("All teams") + allTeams,
            onValueChange = onTeamFilterChange
        )
    }
}
