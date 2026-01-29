package com.brankogeorgiev.data.model

import com.brankogeorgiev.domain.Team
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Match(
    val id: String,
    @SerialName("home_team_id")
    val homeTeamId: String,
    @SerialName("away_team_id")
    val awayTeamId: String,
    @SerialName("home_score")
    val homeScore: Int,
    @SerialName("away_score")
    val awayScore: Int,
    @SerialName("match_date")
    val matchDate: String,
    @SerialName("home_team")
    val homeTeam: Team,
    @SerialName("away_team")
    val awayTeam: Team
)
