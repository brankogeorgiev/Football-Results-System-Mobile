package com.brankogeorgiev.data.create_match

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoalBody(
    @SerialName("player_id") val playerId: String,
    @SerialName("team_id") val teamId: String,
    @SerialName("is_own_goal") val isOwnGoal: Boolean
)