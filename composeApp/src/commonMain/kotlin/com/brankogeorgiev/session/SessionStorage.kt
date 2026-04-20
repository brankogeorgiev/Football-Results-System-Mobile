package com.brankogeorgiev.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.brankogeorgiev.data.auth.UserSession
import kotlinx.coroutines.flow.first
import kotlin.time.Clock

private object Keys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("access_token")
    val USER_ID = stringPreferencesKey("user_id")
    val USER_EMAIL = stringPreferencesKey("user_email")
    val IS_ADMIN = booleanPreferencesKey("is_admin")
    val LOGIN_TIME_MS = longPreferencesKey("login_time_ms")
    val SAVE_CREDENTIALS = booleanPreferencesKey("save_credentials")
    val SAVED_EMAIL = stringPreferencesKey("saved_email")
    val SAVED_PASSWORD = stringPreferencesKey("saved_password")
}

private const val SESSION_DURATION_MS = 3L * 60L * 60L * 1000L

class SessionStorage(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(session: UserSession) {
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = session.accessToken
            prefs[Keys.REFRESH_TOKEN] = session.refreshToken
            prefs[Keys.USER_ID] = session.userId
            prefs[Keys.USER_EMAIL] = session.email
            prefs[Keys.IS_ADMIN] = session.isAdmin
            prefs[Keys.LOGIN_TIME_MS] = Clock.System.now().toEpochMilliseconds()
        }
    }

    suspend fun clearSession() {
        dataStore.edit { prefs ->
            prefs.remove(Keys.ACCESS_TOKEN)
            prefs.remove(Keys.REFRESH_TOKEN)
            prefs.remove(Keys.USER_ID)
            prefs.remove(Keys.USER_EMAIL)
            prefs.remove(Keys.IS_ADMIN)
            prefs.remove(Keys.LOGIN_TIME_MS)
        }
    }

    suspend fun getActiveSession(): UserSession? {
        val prefs = dataStore.data.first()
        val loginTime = prefs[Keys.LOGIN_TIME_MS] ?: return null
        val elapsed = Clock.System.now().toEpochMilliseconds() - loginTime

        if (elapsed > SESSION_DURATION_MS) {
            clearSession()
            return null
        }

        return UserSession(
            accessToken = prefs[Keys.ACCESS_TOKEN] ?: return null,
            refreshToken = prefs[Keys.REFRESH_TOKEN] ?: return null,
            userId = prefs[Keys.USER_ID] ?: "",
            email = prefs[Keys.USER_EMAIL] ?: return null,
            isAdmin = prefs[Keys.IS_ADMIN] ?: false
        )
    }

    suspend fun saveCredentials(email: String, password: String) {
        dataStore.edit { prefs ->
            prefs[Keys.SAVE_CREDENTIALS] = true
            prefs[Keys.SAVED_EMAIL] = email
            prefs[Keys.SAVED_PASSWORD] = password
        }
    }

    suspend fun clearSavedCredentials() {
        dataStore.edit { prefs ->
            prefs[Keys.SAVE_CREDENTIALS] = false
            prefs.remove(Keys.SAVED_EMAIL)
            prefs.remove(Keys.SAVED_PASSWORD)
        }
    }

    suspend fun getSavedCredentials(): SavedCredentials? {
        val prefs = dataStore.data.first()
        if (prefs[Keys.SAVE_CREDENTIALS] != true) return null
        val email = prefs[Keys.SAVED_EMAIL] ?: return null
        val password = prefs[Keys.SAVED_PASSWORD] ?: return null
        return SavedCredentials(email = email, password = password)
    }
}

data class SavedCredentials(val email: String, val password: String)
