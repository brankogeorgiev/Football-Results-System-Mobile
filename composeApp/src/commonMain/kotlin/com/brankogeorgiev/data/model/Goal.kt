package com.brankogeorgiev.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class Goal(
    val id: String = Uuid.random().toHexString(),
    @SerialName("match_id")
    val matchId: String,
    @SerialName("player_id")
    val playerId: String,
    @SerialName("team_id")
    val teamId: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("is_own_goal")
    val isOwnGoal: Boolean,
    @SerialName("player")
    val player: GoalScorer
)