package com.brankogeorgiev.presentation.screen.players

import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.util.RequestState

data class PlayersUiState(
    val players: RequestState<List<Player>> = RequestState.Loading
)