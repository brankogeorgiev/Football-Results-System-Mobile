package com.brankogeorgiev.presentation.screen.stats

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.util.RequestState
import com.brankogeorgiev.util.Result
import kotlinx.coroutines.launch

class StatsViewModel(
    private val client: ApiClient
) : ViewModel() {
    private var _uiState: MutableState<StatsUiState> = mutableStateOf(StatsUiState())
    val uiState: State<StatsUiState> = _uiState

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(data = RequestState.Loading)

            val matchesResponse = client.fetchMatches()
            val goalsResponse = client.fetchGoals()

            if (matchesResponse is Result.Success && goalsResponse is Result.Success) {
                _uiState.value = _uiState.value.copy(
                    data = RequestState.Success(
                        data = StatsUiStateData(
                            matches = matchesResponse.data,
                            goals = goalsResponse.data
                        )
                    )
                )
            } else {
                val errorMessage = when {
                    matchesResponse is Result.Error -> matchesResponse.error.name
                    goalsResponse is Result.Error -> goalsResponse.error.name
                    else -> "Unknown error"
                }

                _uiState.value =
                    _uiState.value.copy(data = RequestState.Error(message = errorMessage))
            }
        }
    }

    private inline fun updateData(block: (StatsUiStateData) -> StatsUiStateData) {
        val current = _uiState.value.data

        if (current is RequestState.Success) {
            _uiState.value = _uiState.value.copy(
                data = RequestState.Success(block(current.data))
            )
        }
    }

    fun updateDateFrom(value: String) {
        updateData { it.copy(dateFrom = value) }
    }

    fun updateDateTo(value: String) {
        updateData { it.copy(dateTo = value) }
    }

    fun updateTeam1Filter(value: String) {
        updateData { it.copy(team1Filter = value) }
    }

    fun updateTeam2Filter(value: String) {
        updateData { it.copy(team2Filter = value) }
    }

    fun clearFilters() {
        updateData {
            it.copy(
                dateFrom = "",
                dateTo = "",
                team1Filter = "All teams",
                team2Filter = "All teams"
            )
        }
    }
}
