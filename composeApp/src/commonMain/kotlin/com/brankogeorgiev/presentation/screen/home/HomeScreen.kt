package com.brankogeorgiev.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.presentation.composable.MatchCard
import com.brankogeorgiev.util.Resource
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(
    uiState: HomeUIState,
    isLoggedIn: Boolean,
    isAdmin: Boolean,
    modifier: Modifier = Modifier
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Past Results",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (isLoggedIn && isAdmin) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(Resource.Icon.ADD),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(text = "Add result")
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.padding(12.dp)
                    .fillMaxSize()
            ) {
                items(items = uiState.matches) {
                    MatchCard(
                        isLoggedIn = isLoggedIn,
                        homeTeam = it.homeTeam,
                        awayTeam = it.awayTeam,
                        homeScore = it.homeScore,
                        awayScore = it.awayScore,
                        date = it.matchDate,
                        onEdit = {},
                        onDelete = {}
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(HomeUIState(), false, true)
    }
}