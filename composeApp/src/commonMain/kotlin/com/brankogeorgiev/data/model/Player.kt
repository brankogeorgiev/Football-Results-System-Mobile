package com.brankogeorgiev.data.model

import com.brankogeorgiev.domain.Team
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("default_team")
    val team: Team,
)