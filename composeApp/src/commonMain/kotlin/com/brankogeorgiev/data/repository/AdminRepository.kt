package com.brankogeorgiev.data.repository

import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.model.AddPlayer
import com.brankogeorgiev.data.model.UserWithRole
import com.brankogeorgiev.data.model.UserWithRoleDto
import com.brankogeorgiev.util.Secrets

class AdminRepository(
    private val apiClient: ApiClient,
    private val authRepository: AuthRepository
) {
    suspend fun getUsers(): List<UserWithRole> {
        return try {
            apiClient.get(
                url = "${Secrets.SUPABASE_URL}/functions/v1/get-users",
                headers = authHeaders()
            )
        } catch (e: Exception) {
            val newToken = authRepository.refreshAccessToken()

            apiClient.get<List<UserWithRole>>(
                url = "${Secrets.SUPABASE_URL}/functions/v1/get-users",
                headers = authHeaders(newToken)
            )

            throw Exception("Failed to fetch users.")
        }
    }

    suspend fun updateUserRole(id: String?, role: String) {
        try {
            val response = apiClient.put<UserWithRoleDto>(
                url = "${Secrets.SUPABASE_URL}/functions/v1/api-user-roles",
                headers = authHeaders(),
                body = mapOf(
                    "id" to id,
                    "role" to role
                )
            )
            println("UPDATE RESPONSE: $response")
        } catch (e: Exception) {
            val newToken = authRepository.refreshAccessToken()

            val response = apiClient.put<UserWithRoleDto>(
                url = "${Secrets.SUPABASE_URL}/functions/v1/api-user-roles",
                headers = authHeaders(token = newToken),
                body = mapOf(
                    "id" to id,
                    "role" to role
                )
            )
            println("UPDATE RESPONSE: $response")
        }
    }

    suspend fun createPlayer(name: String, teamId: String?) {
        return try {
            apiClient.post(
                url = "${Secrets.SUPABASE_URL}/functions/v1/api-players",
                body = AddPlayer(name = name, defaultTeamId = teamId)
            )
        } catch (e: Exception) {
            // TODO: Check
            val newToken = authRepository.refreshAccessToken()

            apiClient.post(
                url = "${Secrets.SUPABASE_URL}/functions/v1/api-players",
                body = AddPlayer(name = name, defaultTeamId = teamId)
            )
        }
    }

    private fun authHeaders(token: String? = null): Map<String, String> {
        val accessToken = token ?: authRepository.getSession()?.accessToken
        ?: throw Exception("User not authenticated")

        return mapOf(
            "Authorization" to "Bearer $accessToken",
            "apiKey" to Secrets.SUPABASE_API_KEY,
            "Accept" to "application/json"
        )
    }
}

