package com.brankogeorgiev.presentation.screen.add_result

import com.brankogeorgiev.data.GoalEvent
import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.domain.Team
import com.brankogeorgiev.util.RequestState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

data class AddResultUiState(
    val teams: RequestState<List<Team>> = RequestState.Loading,
    val players: RequestState<List<Player>> = RequestState.Loading,
    val homeTeam: Team? = null,
    val awayTeam: Team? = null,
    val homeScore: Int = 0,
    val awayScore: Int = 0,
    val homePlayers: List<Player> = emptyList(),
    val awayPlayers: List<Player> = emptyList(),
    val homeGoals: List<GoalEvent> = emptyList(),
    val awayGoals: List<GoalEvent> = emptyList(),
    val saveResult: RequestState<Unit>? = null,
    val matchDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
)