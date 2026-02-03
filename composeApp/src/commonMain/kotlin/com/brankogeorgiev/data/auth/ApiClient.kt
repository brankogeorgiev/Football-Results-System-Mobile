package com.brankogeorgiev.data.auth

import com.brankogeorgiev.data.model.Match
import com.brankogeorgiev.data.model.Player
import com.brankogeorgiev.util.NetworkError
import com.brankogeorgiev.util.Result
import com.brankogeorgiev.util.getBaseUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class ApiClient() {
    companion object {
        private const val API_MATCHES = "/api-matches"
        private const val API_PLAYERS = "/api-players"
    }

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend inline fun <reified T> post(
        url: String,
        headers: Map<String, String> = emptyMap(),
        body: Any
    ): T {
        return client.post(url) {
            headers.forEach { (k, v) -> header(k, v) }
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    suspend inline fun <reified T> get(
        url: String,
        headers: Map<String, String> = emptyMap()
    ): T {
        return client.get(url) {
            headers.forEach { (k, v) -> header(k, v) }
        }.body()
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

    suspend fun fetchMatches(): Result<List<Match>, NetworkError> =
        safeGet<List<Match>>(client, getBaseUrl() + API_MATCHES)

    suspend fun fetchPlayers() =
        safeGet<List<Player>>(client, getBaseUrl() + API_PLAYERS)
}