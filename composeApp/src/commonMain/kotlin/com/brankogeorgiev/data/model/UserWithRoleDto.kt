package com.brankogeorgiev.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserWithRoleDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val role: String,
    @SerialName("created_at")
    val createdAt: String
)