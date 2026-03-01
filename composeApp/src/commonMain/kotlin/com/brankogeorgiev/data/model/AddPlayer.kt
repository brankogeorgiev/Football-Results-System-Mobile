package com.brankogeorgiev.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddPlayer(
    val name: String,
    @SerialName("default_team_id")
    val defaultTeamId: String? = null
)