package com.miguelveliz.minipos.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// ¡IMPORTANTE! Ajusta esta URL al puerto que uses en Mockoon.
//const val BASE_URL = "http://localhost:3030"
const val BASE_URL = "http://10.0.2.2:3030"

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }
}