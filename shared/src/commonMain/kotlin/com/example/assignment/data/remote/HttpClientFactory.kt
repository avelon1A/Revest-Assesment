package com.example.assignment.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val AppJson: Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
}

private val clientConfig: HttpClientConfig<*>.() -> Unit = {
    install(ContentNegotiation) {
        json(AppJson)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 30_000
    }
    install(Logging) {
        level = LogLevel.INFO
    }
}

fun createHttpClient(engine: HttpClientEngine? = null): HttpClient =
    if (engine != null) HttpClient(engine, clientConfig) else HttpClient(clientConfig)
