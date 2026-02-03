package com.brankogeorgiev.data.auth

data class UserSession(
    val accessToken: String,
    val userId: String,
    val isAdmin: Boolean
)
