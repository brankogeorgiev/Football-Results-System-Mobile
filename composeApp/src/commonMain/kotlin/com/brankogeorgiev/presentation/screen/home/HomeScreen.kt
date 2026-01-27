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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brankogeorgiev.domain.Match
import com.brankogeorgiev.presentation.composable.MatchCard
import com.brankogeorgiev.util.Resource
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Clock

@Composable
fun HomeScreen(
    isLoggedIn: Boolean,
    updateIsLoggedIn: () -> Unit,
    isAdmin: Boolean,
    modifier: Modifier = Modifier
) {
    val match = Match(
        homeTeam = "Beli", awayTeam = "Crni", homeScore = 4, awayScore = 3,
        matchDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    )
    val matchList = listOf(
        match, match, match, match, match, match, match, match, match
    )

    Scaffold { paddingValues ->
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
                    fontWeight = FontWeight.Black
                )

                if (isLoggedIn && isAdmin) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0BDA51),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(Resource.Icon.ADD),
                            contentDescription = ""
                        )
                        Text(text = "Add result")
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.padding(12.dp)
                    .fillMaxSize()
            ) {
                items(items = matchList) {
                    MatchCard(
                        isLoggedIn = isLoggedIn,
                        onUpdateIsLoggedIn = updateIsLoggedIn,
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
        HomeScreen(false, {}, true)
    }
}