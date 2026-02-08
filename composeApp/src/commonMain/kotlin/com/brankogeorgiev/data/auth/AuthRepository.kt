package com.brankogeorgiev.data.auth

import com.brankogeorgiev.util.Secrets

class AuthRepository(private val apiClient: ApiClient) {
    private val sessionStore = SessionStore()
    private var session: UserSession? = null

    fun getSession(): UserSession? = session

    suspend fun login(email: String, password: String) {
        val accessToken = try {
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
            ).access_token
        } catch (e: Exception) {
            throw Exception("Invalid credentials")
        }

        val user = fetchUser(accessToken, email)

        session = UserSession(
            accessToken = accessToken,
            userId = user?.id ?: email,
            isAdmin = user?.role == "admin"
        )

        sessionStore.saveSession(session)
    }

    suspend fun signUp(email: String, password: String) {
        try {
            apiClient.post<Unit>(
                url = "${Secrets.SUPABASE_URL}/auth/v1/signup",
                headers = mapOf(
                    "apikey" to Secrets.SUPABASE_API_KEY,
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
                url = "${Secrets.SUPABASE_URL}/functions/v1/get-users",
                headers = mapOf(
                    "Authorization" to "Bearer $accessToken",
                    "apikey" to Secrets.SUPABASE_API_KEY,
                    "Accept" to "application/json"
                )
            )
            users.find { it.email == email }
        } catch (_: Exception) {
            null
        }
    }

    fun loadSavedSession(): UserSession? {
        session = sessionStore.loadSavedSession()
        return session
    }

    fun clearSavedSession() {
        session = null
        sessionStore.clearSavedSession()
    }
}