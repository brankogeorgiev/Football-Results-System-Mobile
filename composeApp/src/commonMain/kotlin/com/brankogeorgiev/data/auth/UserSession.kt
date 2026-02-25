package com.brankogeorgiev.data.auth

data class UserSession(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val email: String,
    val isAdmin: Boolean
)
