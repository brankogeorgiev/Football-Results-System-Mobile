package com.brankogeorgiev.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserWithRole(
    val id: String,
    val email: String,
    val role: String?,
    val roleId: String?
)