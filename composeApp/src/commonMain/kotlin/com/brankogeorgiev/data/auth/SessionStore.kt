package com.brankogeorgiev.data.auth

import com.russhwolf.settings.Settings

class SessionStore {
    private var settings = Settings()

    fun saveSession(session: UserSession?) {
        if (session == null) return

        settings.putString("accessToken", session.accessToken)
        settings.putString("userId", session.userId)
        settings.putBoolean("isAdmin", session.isAdmin)
    }

    fun loadSavedSession(): UserSession? {
        val accessToken = settings.getStringOrNull("accessToken") ?: return null
        val refreshToken = settings.getStringOrNull("refreshToken") ?: return null
        val userId = settings.getStringOrNull("userId") ?: return null
        val email = settings.getStringOrNull("email") ?: return null
        val isAdmin = settings.getBoolean("isAdmin", false)

        return UserSession(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = userId,
            email = email,
            isAdmin = isAdmin
        )
    }

    fun clearSavedSession() {
        settings.remove("accessToken")
        settings.remove("userId")
        settings.remove("isAdmin")
    }
}