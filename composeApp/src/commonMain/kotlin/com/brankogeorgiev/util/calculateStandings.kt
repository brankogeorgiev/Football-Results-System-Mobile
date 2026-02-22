package com.brankogeorgiev.util

import com.brankogeorgiev.data.model.Match
import com.brankogeorgiev.domain.TeamStanding
import kotlinx.datetime.LocalDate

fun calculateStandings(
    matches: List<Match>,
    dateFrom: String,
    dateTo: String,
    team1Filter: String,
    team2Filter: String
): List<TeamStanding> {
    val filteredByDate = matches.filter { match ->
        val matchDate = LocalDate.parse(match.matchDate)

        val afterFrom = if (dateFrom.isBlank()) {
            true
        } else {
            val parts = dateFrom.split(".")
            val fromDate = LocalDate(
                year = parts[2].toInt(),
                monthNumber = parts[1].toInt(),
                dayOfMonth = parts[0].toInt()
            )
            matchDate >= fromDate
        }

        val beforeTo = if (dateTo.isBlank()) {
            true
        } else {
            val parts = dateTo.split(".")
            val toDate = LocalDate(
                year = parts[2].toInt(),
                monthNumber = parts[1].toInt(),
                dayOfMonth = parts[0].toInt()
            )
            matchDate <= toDate
        }

        afterFrom && beforeTo
    }

    val filteredMatches = filteredByDate.filter { match ->
        val home = match.homeTeam.name
        val away = match.awayTeam.name
        val team1 = team1Filter == "All teams" || home == team1Filter || away == team1Filter
        val team2 = team2Filter == "All teams" || home == team2Filter || away == team2Filter
        team1 && team2
    }

    data class TempStanding(
        var played: Int = 0,
        var wins: Int = 0,
        var draws: Int = 0,
        var losses: Int = 0,
        var goalsFor: Int = 0,
        var goalsAgaints: Int = 0
    )

    val table = mutableMapOf<String, TempStanding>()

    fun getOrCreate(team: String): TempStanding = table.getOrPut(team) { TempStanding() }

    filteredMatches.forEach { match ->
        val homeName = match.homeTeam.name
        val awayName = match.awayTeam.name

        val homeScore = match.homeScore
        val awayScore = match.awayScore

        val home = getOrCreate(homeName)
        val away = getOrCreate(awayName)

        home.played++
        away.played++

        home.goalsFor += homeScore
        home.goalsAgaints += awayScore

        away.goalsFor += awayScore
        away.goalsAgaints += homeScore

        when {
            homeScore > awayScore -> {
                home.wins++
                away.losses++
            }

            homeScore < awayScore -> {
                home.losses++
                away.wins++
            }

            else -> {
                home.draws++
                away.draws++
            }
        }
    }
    val standings = table.map { (team, stats) ->
        val goalDiff = stats.goalsFor - stats.goalsAgaints
        val points = stats.wins * 3 + stats.draws

        TeamStanding(
            position = 0,
            team = team,
            played = stats.played,
            wins = stats.wins,
            draws = stats.draws,
            losses = stats.losses,
            goalsFor = stats.goalsFor,
            goalsAgainst = stats.goalsAgaints,
            goalDifference = goalDiff,
            points = points
        )
    }.sortedWith(
        compareByDescending<TeamStanding> { it.points }
            .thenByDescending { it.goalDifference }
            .thenByDescending { it.goalsFor }
    ).mapIndexed { index, standing ->
        standing.copy(position = index + 1)
    }

    return standings
}