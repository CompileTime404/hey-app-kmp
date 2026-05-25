package com.dorianbanic.core.data.networking

object UrlConstant {
    val BASE_URL_HTTP: String
        get() = BackendConfig.apiBaseUrl

    val BASE_URL_WS: String
        get() = BackendConfig.wsBaseUrl
}