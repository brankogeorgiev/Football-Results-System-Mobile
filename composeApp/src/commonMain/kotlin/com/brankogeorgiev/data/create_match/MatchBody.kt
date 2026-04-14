package com.brankogeorgiev.data.create_match

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MatchBody(
    @SerialName("home_team_id") val homeTeamId: String,
    @SerialName("away_team_id") val awayTeamId: String,
    @SerialName("home_score") val homeScore: Int,
    @SerialName("away_score") val awayScore: Int,
    @SerialName("match_date") val matchDate: String
)