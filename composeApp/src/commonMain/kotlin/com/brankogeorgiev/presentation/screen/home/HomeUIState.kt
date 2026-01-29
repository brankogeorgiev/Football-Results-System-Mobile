package com.brankogeorgiev.presentation.screen.home

import com.brankogeorgiev.data.model.Match
import com.brankogeorgiev.util.RequestState

data class HomeUIState(
    val matches: RequestState<List<Match>> = RequestState.Loading
)
