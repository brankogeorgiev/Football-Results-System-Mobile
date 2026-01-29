package com.brankogeorgiev.data.network

import com.brankogeorgiev.util.Secrets
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(engine: HttpClientEngine): HttpClient {
    return HttpClient(engine) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        defaultRequest {
            header("Authorization", "Bearer ${Secrets.SUPABASE_API_KEY}")
            header("apiKey", Secrets.SUPABASE_API_KEY)
            header("Content-Type", "application/json")
        }
    }
}

fun getBaseUrl(): String {
    return Secrets.BASE_URL
}

