package com.brankogeorgiev.data.network

import com.brankogeorgiev.data.model.Match
import com.brankogeorgiev.util.NetworkError
import com.brankogeorgiev.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

class ApiClient(
    private val httpClient: HttpClient
) {
    companion object {
        private const val API_MATCHES = "/api-matches"
    }

    suspend fun fetchMatches(): Result<List<Match>, NetworkError> {
        val response = try {
            httpClient.get(urlString = getBaseUrl() + API_MATCHES) { }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> {
                val matches = response.body<List<Match>>()
                Result.Success(data = matches)
            }

            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> Result.Error(NetworkError.CONFLICT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}