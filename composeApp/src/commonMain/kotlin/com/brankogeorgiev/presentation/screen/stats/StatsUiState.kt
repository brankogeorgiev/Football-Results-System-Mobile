package com.brankogeorgiev.presentation.screen.stats

import com.brankogeorgiev.util.RequestState

data class StatsUiState(
    val data: RequestState<StatsUiStateData> = RequestState.Loading
)