package com.brankogeorgiev.presentation.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.domain.Match
import com.brankogeorgiev.presentation.composable.MatchCard
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var isLoggedIn by remember { mutableStateOf(false) }

    val match = Match(
        homeTeam = "Beli", awayTeam = "Crni", homeScore = 4, awayScore = 3,
        matchDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    )
    val matchList = listOf(
        match, match
    )

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.padding(horizontal = 40.dp),
                text = "Past Results",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )

            LazyColumn(
                modifier = Modifier.padding(20.dp)
                    .fillMaxSize()
            ) {
                items(items = matchList) {
                    MatchCard(
                        isLoggedIn = isLoggedIn,
                        onUpdateIsLoggedIn = { isLoggedIn = !isLoggedIn },
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
        HomeScreen()
    }
}