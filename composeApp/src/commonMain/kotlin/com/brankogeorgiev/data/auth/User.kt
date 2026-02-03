package com.brankogeorgiev.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val role: String? = null,
    val roleId: String? = null
)
