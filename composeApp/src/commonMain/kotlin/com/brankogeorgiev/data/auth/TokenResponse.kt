package com.brankogeorgiev.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val expires_in: Int
)
