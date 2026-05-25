package com.dorianbanic.core.data.networking

actual object BackendConfig {
    private val host = System.getProperty("heyapp.backend.host") ?: "localhost"
    private val port = System.getProperty("heyapp.backend.port") ?: "8080"

    actual val apiBaseUrl: String = "http://$host:$port/api"
    actual val wsBaseUrl: String = "ws://$host:$port/ws"
}