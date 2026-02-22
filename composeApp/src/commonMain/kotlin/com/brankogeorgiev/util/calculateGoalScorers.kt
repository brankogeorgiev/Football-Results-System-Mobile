package com.brankogeorgiev.util

import com.brankogeorgiev.domain.TopScorer
import com.brankogeorgiev.presentation.screen.stats.StatsUiStateData

fun calculateGoalScorers(data: StatsUiStateData, isOwnGoal: Boolean): List<TopScorer> {
    val filteredMatches = data.matches.filter { match ->
        val matchesTeam1 = data.team1Filter == "All teams" ||
                match.homeTeam.name == data.team1Filter ||
                match.awayTeam.name == data.team1Filter

        val matchesTeam2 = data.team2Filter == "All teams" ||
                match.homeTeam.name == data.team2Filter ||
                match.awayTeam.name == data.team2Filter

        val matchesDate =
            (data.dateFrom.isBlank() || match.matchDate >= transformDate(data.dateFrom)) &&
                    (data.dateTo.isBlank() || match.matchDate <= transformDate(data.dateTo))

        matchesTeam1 && matchesTeam2 && matchesDate
    }

    val validMatchIds = filteredMatches.map { it.id }.toSet()

    return data.goals.filter { goal ->
        goal.isOwnGoal == isOwnGoal && goal.matchId in validMatchIds
    }
        .groupBy { it.player.id }
        .map { (_, goals) ->
            TopScorer(name = goals.first().player.name, goals = goals.size)
        }.sortedByDescending { it.goals }
}

private fun transformDate(date: String): String {
    val parts = date.split(".")
    if (parts.size != 3) return date

    val (day, month, year) = parts
    return "$year-$month-$day"
}