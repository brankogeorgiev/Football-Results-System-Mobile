package com.brankogeorgiev.data.create_match

import kotlinx.serialization.Serializable

@Serializable
data class CreateMatchRequest(
    val match: MatchBody,
    val players: List<MatchPlayerBody>,
    val goals: List<GoalBody>
)