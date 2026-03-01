package com.brankogeorgiev.data.auth

import com.brankogeorgiev.data.model.Goal
import com.brankogeorgiev.data.model.Match
import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.util.NetworkError
import com.brankogeorgiev.util.Result
import com.brankogeorgiev.util.getBaseUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class ApiClient() {
    companion object {
        private const val API_MATCHES = "/api-matches"
        private const val API_PLAYERS = "/api-players"
        private const val API_GOALS = "/api-goals"
    }

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend inline fun <reified T> requestSafe(
        method: HttpMethod,
        url: String,
        headers: Map<String, String> = emptyMap(),
        body: Any? = null
    ): Result<T, NetworkError> {
        val response: HttpResponse = try {
            client.request(url) {
                this.method = method
                headers.forEach { (k, v) -> header(k, v) }
                if (body != null) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }
            }
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

    suspend inline fun <reified T> get(url: String, headers: Map<String, String> = emptyMap()) =
        requestSafe<T>(method = HttpMethod.Get, url = url, headers = headers)

    suspend inline fun <reified T> post(
        url: String,
        headers: Map<String, String> = emptyMap(),
        body: Any
    ) = requestSafe<T>(method = HttpMethod.Post, url = url, headers = headers, body = body)

    suspend inline fun <reified T> put(
        url: String,
        headers: Map<String, String> = emptyMap(),
        body: Any
    ) = requestSafe<T>(method = HttpMethod.Put, url = url, headers = headers, body = body)

    suspend inline fun <reified T> delete(
        url: String,
        headers: Map<String, String> = emptyMap()
    ) = requestSafe<T>(HttpMethod.Delete, url = url, headers = headers)

    suspend fun fetchMatches(): Result<List<Match>, NetworkError> =
        get(url = getBaseUrl() + API_MATCHES)

    suspend fun fetchPlayers(): Result<List<Player>, NetworkError> =
        get(url = getBaseUrl() + API_PLAYERS)

    suspend fun fetchGoals(): Result<List<Goal>, NetworkError> =
        get(url = getBaseUrl() + API_GOALS)

    suspend fun deletePlayer(playerId: String): Result<Unit, NetworkError> =
        delete(url = getBaseUrl() + "$API_PLAYERS/$playerId")
}