package com.brankogeorgiev.data.network

import com.brankogeorgiev.data.model.Match
import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.util.NetworkError
import com.brankogeorgiev.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

class ApiClient(
    private val httpClient: HttpClient
) {
    companion object {
        private const val API_MATCHES = "/api-matches"
        private const val API_PLAYERS = "/api-players"
    }

    suspend inline fun <reified T> safeGet(
        httpClient: HttpClient,
        url: String
    ): Result<T, NetworkError> {
        val response: HttpResponse = try {
            httpClient.get(url)
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> {
                try {
                    Result.Success(response.body<T>())
                } catch (e: SerializationException) {
                    Result.Error(NetworkError.SERIALIZATION)
                }
            }

            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> Result.Error(NetworkError.CONFLICT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }

    suspend fun fetchMatches() =
        safeGet<List<Match>>(httpClient, getBaseUrl() + API_MATCHES)

    suspend fun fetchPlayers() =
        safeGet<List<Player>>(httpClient, getBaseUrl() + API_PLAYERS)
}