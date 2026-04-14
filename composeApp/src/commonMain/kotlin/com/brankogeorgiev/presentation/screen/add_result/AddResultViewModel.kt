package com.brankogeorgiev.presentation.screen.add_result

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brankogeorgiev.data.GoalEvent
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.UserSession
import com.brankogeorgiev.data.create_match.CreateMatchRequest
import com.brankogeorgiev.data.create_match.GoalBody
import com.brankogeorgiev.data.create_match.MatchBody
import com.brankogeorgiev.data.create_match.MatchPlayerBody
import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.data.repository.AdminRepository
import com.brankogeorgiev.domain.Team
import com.brankogeorgiev.util.RequestState
import com.brankogeorgiev.util.Result
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class AddResultViewModel(
    private val client: ApiClient,
    private val session: UserSession?,
    private val adminRepository: AdminRepository
) : ViewModel() {
    private var _uiState: MutableState<AddResultUiState> = mutableStateOf(AddResultUiState())
    val uiState: State<AddResultUiState> = _uiState

    init {
        loadTeams()
        loadPlayers()
    }

    fun setHomeTeam(team: Team) {
        _uiState.value = _uiState.value.copy(homeTeam = team)
    }

    fun setAwayTeam(team: Team) {
        _uiState.value = _uiState.value.copy(awayTeam = team)
    }

    fun setHomeScore(score: Int) {
        _uiState.value = _uiState.value.copy(homeScore = score)
    }

    fun setAwayScore(score: Int) {
        _uiState.value = _uiState.value.copy(awayScore = score)
    }

    fun setMatchDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(matchDate = date)
    }

    fun onPitchSlotsChanged(homePlayers: List<Player>, awayPlayers: List<Player>) {
        _uiState.value = _uiState.value.copy(
            homePlayers = homePlayers,
            awayPlayers = awayPlayers
        )
    }

    fun onGoalEventChanged(homeEvents: List<GoalEvent>, awayEvents: List<GoalEvent>) {
        _uiState.value = _uiState.value.copy(
            homeGoals = homeEvents,
            awayGoals = awayEvents
        )
    }

    fun addPlayer(name: String, teamId: String) {
        viewModelScope.launch {
            try {
                adminRepository.createPlayer(name = name, teamId = teamId)
                loadPlayers()
            } catch (e: Exception) {

            }
        }
    }

    fun saveResult() {
        val state = _uiState.value
        val homeTeam = state.homeTeam ?: return
        val awayTeam = state.awayTeam ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(saveResult = RequestState.Loading)

            try {
                val dateStr = state.matchDate.run {
                    "$year=${monthNumber.toString().padStart(2, '0')}-${
                        dayOfMonth.toString().padStart(2, '0')
                    }"
                }

                val matchPlayers = (
                        state.homePlayers.map { MatchPlayerBody(it.id, homeTeam.id) } +
                                state.awayPlayers.map { MatchPlayerBody(it.id, awayTeam.id) }
                        )

                val goals = state.homeGoals.map { goal ->
                    GoalBody(
                        playerId = goal.player.id,
                        teamId = homeTeam.id,
                        isOwnGoal = goal.isOwnGoal
                    )
                } + state.awayGoals.map { goal ->
                    GoalBody(
                        playerId = goal.player.id,
                        teamId = awayTeam.id,
                        isOwnGoal = goal.isOwnGoal
                    )
                }

                val body = CreateMatchRequest(
                    match = MatchBody(
                        homeTeamId = homeTeam.id,
                        awayTeamId = awayTeam.id,
                        homeScore = state.homeScore,
                        awayScore = state.awayScore,
                        matchDate = dateStr
                    ),
                    players = matchPlayers,
                    goals = goals
                )

                when (val response = adminRepository.createMatchFull(body)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            saveResult = RequestState.Success(Unit)
                        )
                    }

                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            saveResult = RequestState.Error(response.error.name)
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    saveResult = RequestState.Error(message = e.message ?: "Unknown error")
                )
            }
        }
    }

    private fun loadTeams() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(teams = RequestState.Loading)

            when (val response = client.fetchTeams()) {
                is Result.Success -> {
                    val teams = response.data
                    _uiState.value = _uiState.value.copy(
                        teams = RequestState.Success(data = teams),
                        homeTeam = _uiState.value.homeTeam ?: teams.getOrNull(0),
                        awayTeam = _uiState.value.awayTeam ?: teams.getOrNull(1)
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        teams = RequestState.Error(message = response.error.name)
                    )
                }
            }
        }
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(players = RequestState.Loading)

            when (val response = client.fetchPlayers()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        players = RequestState.Success(data = response.data)
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        players = RequestState.Error(message = response.error.name)
                    )
                }
            }

        }
    }
}