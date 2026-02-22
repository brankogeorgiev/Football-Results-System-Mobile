package com.brankogeorgiev.presentation.screen.stats

import com.brankogeorgiev.data.model.Goal
import com.brankogeorgiev.data.model.Match

data class StatsUiStateData(
    val matches: List<Match> = emptyList(),
    val goals: List<Goal> = emptyList(),
    val dateFrom: String = "",
    val dateTo: String = "",
    val team1Filter: String = "All teams",
    val team2Filter: String = "All teams"
)