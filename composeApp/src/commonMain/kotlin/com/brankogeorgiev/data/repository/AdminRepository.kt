package com.brankogeorgiev.data.repository

import com.brankogeorgiev.data.auth.ApiClient
import com.brankogeorgiev.data.model.AddPlayer
import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.data.model.UserWithRole
import com.brankogeorgiev.data.model.UserWithRoleDto
import com.brankogeorgiev.util.NetworkError
import com.brankogeorgiev.util.Result
import com.brankogeorgiev.util.Secrets

class AdminRepository(
    private val apiClient: ApiClient,
    private val authRepository: AuthRepository
) {
    suspend fun getUsers(): Result<List<UserWithRole>, NetworkError> {
        val result = apiClient.get<List<UserWithRole>>(
            url = "${Secrets.SUPABASE_URL}/functions/v1/get-users",
            headers = authHeaders()
        )

        if (result is Result.Error && result.error == NetworkError.UNAUTHORIZED) {
            val newToken = authRepository.refreshAccessToken()
            return apiClient.get(
                url = "${Secrets.SUPABASE_URL}/functions/v1/get-users",
                headers = authHeaders(newToken)
            )
        }

        return result
    }

    suspend fun updateUserRole(id: String?, role: String): Result<UserWithRoleDto, NetworkError> {
        val result = apiClient.put<UserWithRoleDto>(
            url = "${Secrets.SUPABASE_URL}/functions/v1/api-user-roles",
            headers = authHeaders(),
            body = mapOf("id" to id, "role" to role)
        )

        if (result is Result.Error && result.error == NetworkError.UNAUTHORIZED) {
            val newToken = authRepository.refreshAccessToken()
            return apiClient.put(
                url = "${Secrets.SUPABASE_URL}/functions/v1/api-user-roles",
                headers = authHeaders(token = newToken),
                body = mapOf("id" to id, "role" to role)
            )
        }

        return result
    }

    suspend fun createPlayer(name: String, teamId: String?): Result<Unit, NetworkError> {
        val result = apiClient.post<Unit>(
            url = "${Secrets.SUPABASE_URL}/functions/v1/api-players",
            headers = authHeaders(),
            body = AddPlayer(name = name, defaultTeamId = teamId)
        )

        if (result is Result.Error && result.error == NetworkError.UNAUTHORIZED) {
            val newToken = authRepository.refreshAccessToken()
            return apiClient.post(
                url = "${Secrets.SUPABASE_URL}/functions/v1/api-players",
                headers = authHeaders(newToken),
                body = AddPlayer(name = name, defaultTeamId = teamId)
            )
        }

        return result
    }

    suspend fun updatePlayer(
        id: String,
        name: String,
        teamId: String?
    ): Result<Player, NetworkError> {
        return apiClient.put(
            url = "${Secrets.SUPABASE_URL}/functions/v1/api-players?id=$id",
            headers = authHeaders(),
            body = mapOf("name" to name, "default_team_id" to teamId),
        )
    }

    suspend fun deletePlayer(id: String): Result<Unit, NetworkError> {
        val result = apiClient.delete<Unit>(
            url = "${Secrets.SUPABASE_URL}/functions/v1/api-players?id=$id",
            headers = authHeaders()
        )

        if (result is Result.Error && result.error == NetworkError.UNAUTHORIZED) {
            val newToken = authRepository.refreshAccessToken()
            return apiClient.delete(
                url = "${Secrets.SUPABASE_URL}/functions/v1/api-players?id=$id",
                headers = authHeaders(newToken)
            )
        }

        return result
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