package com.brankogeorgiev.data.repository

import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.auth.SessionStore
import com.brankogeorgiev.data.auth.TokenResponse
import com.brankogeorgiev.data.auth.User
import com.brankogeorgiev.data.auth.UserSession
import com.brankogeorgiev.util.Secrets

class AuthRepository(private val apiClient: ApiClient) {
    private val sessionStore = SessionStore()
    private var session: UserSession? = null

    fun getSession(): UserSession? = session

    suspend fun login(email: String, password: String) {
        val tokenResponse = try {
            apiClient.post<TokenResponse>(
                url = "${Secrets.SUPABASE_URL}/auth/v1/token?grant_type=password",
                headers = mapOf(
                    "apikey" to Secrets.SUPABASE_API_KEY,
                    "Content-Type" to "application/json"
                ),
                body = mapOf(
                    "email" to email,
                    "password" to password
                )
            )
        } catch (e: Exception) {
            throw Exception("Invalid credentials")
        }

        val accessToken = tokenResponse.access_token
        val refreshToken = tokenResponse.refresh_token

        val user = fetchUser(accessToken, email)

        session = UserSession(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = user?.id ?: email,
            email = email,
            isAdmin = user?.role == "admin"
        )

        sessionStore.saveSession(session)
    }

    suspend fun signUp(email: String, password: String) {
        try {
            apiClient.post<Unit>(
                url = "${Secrets.Companion.SUPABASE_URL}/auth/v1/signup",
                headers = mapOf(
                    "apikey" to Secrets.Companion.SUPABASE_API_KEY,
                    "Content-Type" to "application/json"
                ),
                body = mapOf(
                    "email" to email,
                    "password" to password
                )
            )

            login(email, password)

        } catch (e: Exception) {
            throw Exception("Sign up failed. Please try again.")
        }
    }

    private suspend fun fetchUser(
        accessToken: String,
        email: String
    ): User? {
        return try {
            val users: List<User> = apiClient.get(
                url = "${Secrets.Companion.SUPABASE_URL}/functions/v1/get-users",
                headers = mapOf(
                    "Authorization" to "Bearer $accessToken",
                    "apikey" to Secrets.Companion.SUPABASE_API_KEY,
                    "Accept" to "application/json"
                )
            )
            users.find { it.email == email }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun refreshAccessToken(): String {
        val currentSession = session ?: throw Exception("No session")

        val response = apiClient.post<TokenResponse>(
            url = "${Secrets.Companion.SUPABASE_URL}/auth/v1/token?grant_type=refresh_token",
            headers = mapOf(
                "apiKey" to Secrets.Companion.SUPABASE_API_KEY,
                "Content-Type" to "application/json"
            ),
            body = mapOf("refresh_token" to currentSession.refreshToken)
        )

        session = currentSession.copy(
            accessToken = response.access_token,
            refreshToken = response.refresh_token
        )

        sessionStore.saveSession(session)
        return response.access_token
    }

    fun loadSavedSession(): UserSession? {
        session = sessionStore.loadSavedSession()
        return session
    }

    fun clearSavedSession() {
        session = null
        sessionStore.clearSavedSession()
    }

    fun getCurrentUserEmail(): String? {
        return session?.email
    }
}