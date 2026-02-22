package com.brankogeorgiev.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GoalScorer(
    val id: String,
    val name: String
)