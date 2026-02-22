package com.brankogeorgiev.presentation.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.util.Resource
import com.brankogeorgiev.util.toMillisOrNull
import org.jetbrains.compose.resources.painterResource

@Composable
fun FiltersCard(
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    dateFrom: String,
    dateTo: String,
    team1Filter: String,
    team2Filter: String,
    allTeams: List<String>,
    onDateFromChange: (String) -> Unit,
    onDateToChange: (String) -> Unit,
    onTeam1FilterChange: (String) -> Unit,
    onTeam2FilterChange: (String) -> Unit,
    onClearFilters: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onExpandChange(!expanded) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(Resource.Icon.FILTER),
                        contentDescription = "Filter",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Filters",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Icon(
                    painter = painterResource(
                        if (expanded) Resource.Icon.ARROW_UP
                        else Resource.Icon.ARROW_DOWN
                    ),
                    contentDescription = "Expand",
                    modifier = Modifier.size(20.dp).then(if (expanded) Modifier else Modifier)
                )
            }
        }

        FilterCardFields(
            expanded = expanded,
            dateFrom = dateFrom,
            onDateFromChange = onDateFromChange,
            dateTo = dateTo,
            onDateToChange = onDateToChange,
            team1Filter = team1Filter,
            allTeams = allTeams,
            onTeam1FilterChange = onTeam1FilterChange,
            team2Filter = team2Filter,
            onTeam2FilterChange = onTeam2FilterChange,
            onClearFilters = onClearFilters
        )
    }
}

@Composable
private fun FilterCardFields(
    expanded: Boolean,
    dateFrom: String,
    onDateFromChange: (String) -> Unit,
    dateTo: String,
    onDateToChange: (String) -> Unit,
    team1Filter: String,
    allTeams: List<String>,
    onTeam1FilterChange: (String) -> Unit,
    team2Filter: String,
    onTeam2FilterChange: (String) -> Unit,
    onClearFilters: () -> Unit
) {
    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
        exit = shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DatePickerField(
                    date = dateFrom,
                    text = "From",
                    onDateChange = onDateFromChange,
                    endDateMillis = dateTo.toMillisOrNull(),
                    modifier = Modifier.weight(1f)
                )

                DatePickerField(
                    date = dateTo,
                    text = "To",
                    onDateChange = onDateToChange,
                    startDateMillis = dateFrom.toMillisOrNull(),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // TODO: Implement team change
                TeamPickerField(
                    teamFilter = team1Filter,
                    team = "Team 1",
                    allTeams = allTeams.filter { it != team2Filter },
                    onTeamFilterChange = onTeam1FilterChange,
                    modifier = Modifier.weight(1f)
                )

                // TODO: Implement team change
                TeamPickerField(
                    teamFilter = team2Filter,
                    team = "Team 2",
                    allTeams = allTeams.filter { it != team1Filter },
                    onTeamFilterChange = onTeam2FilterChange,
                    modifier = Modifier.weight(1f)
                )
            }

            TextButton(
                onClick = onClearFilters,
                modifier = Modifier.align(alignment = Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Clear Filters")
            }
        }
    }
}
