package com.brankogeorgiev.presentation.screen.players

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brankogeorgiev.data.network.ApiClient
import com.brankogeorgiev.util.RequestState
import com.brankogeorgiev.util.Result
import kotlinx.coroutines.launch

class PlayersViewModel(
    private val client: ApiClient
) : ViewModel() {
    private var _uiState: MutableState<PlayersUiState> = mutableStateOf(PlayersUiState())
    val uiState: State<PlayersUiState> = _uiState

    init {
        loadPlayers()
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