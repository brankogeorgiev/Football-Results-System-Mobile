package com.brankogeorgiev.data.auth

import com.brankogeorgiev.util.Secrets

class AuthRepository(private val apiClient: ApiClient) {
    private var sessionStore = SessionStore()
    private var session: UserSession? = null

    fun getSession(): UserSession? = session

    suspend fun login(email: String, password: String) {
        val accessToken = try {
            val tokenResponse = apiClient.post<TokenResponse>(
                url = Secrets.SUPABASE_URL + "/auth/v1/token?grant_type=password",
                headers = mapOf(
                    "apiKey" to Secrets.SUPABASE_API_KEY,
                    "Content-Type" to "application/json"
                ),
                body = mapOf(
                    "email" to email,
                    "password" to password
                )
            )
            tokenResponse.access_token
        } catch (e: Exception) {
            throw Exception("Invalid credentials")
        }

        val user: User? =
            try {
                val users: List<User> = apiClient.get<List<User>>(
                    url = Secrets.SUPABASE_URL + "/functions/v1/get-users",
                    headers = mapOf(
                        "Authorization" to "Bearer $accessToken",
                        "apiKey" to Secrets.SUPABASE_API_KEY,
                        "Content-Type" to "application/json",
                        "Accept" to "application/json"
                    )
                )
                users.find { it.email == email } ?: throw Exception("User not found")
            } catch (e: Exception) {
                null
            }

        session = UserSession(
            accessToken = accessToken,
            userId = user?.id ?: email,
            isAdmin = user?.role == "admin"
        )

        sessionStore.saveSession(session)
    }

    fun loadSavedSession(): UserSession? {
        session = sessionStore.loadSavedSession()
        return session
    }

    fun saveSession(session: UserSession?) = sessionStore.saveSession(session)

    fun clearSavedSession() {
        session = null
        sessionStore.clearSavedSession()
    }
}