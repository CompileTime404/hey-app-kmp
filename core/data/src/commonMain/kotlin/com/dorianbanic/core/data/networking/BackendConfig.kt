package com.dorianbanic.core.data.networking

expect object BackendConfig {
    val apiBaseUrl: String
    val wsBaseUrl: String
}