package com.dorianbanic.core.data.networking

import com.dorianbanic.core.data.BuildKonfig
import com.dorianbanic.core.data.dto.AuthInfoSerializable
import com.dorianbanic.core.data.dto.request.RefreshRequest
import com.dorianbanic.core.data.mappers.toAuthInfo
import com.dorianbanic.core.domain.auth.SessionStorage
import com.dorianbanic.core.domain.logging.HeyappLogger
import com.dorianbanic.core.domain.util.onFailure
import com.dorianbanic.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val heyappLogger: HeyappLogger,
    private val sessionStorage: SessionStorage
) {

    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        heyappLogger.debug(message)
                    }
                }
                level = LogLevel.ALL
            }
            install(WebSockets) {
                pingIntervalMillis = 20_000L
            }
            defaultRequest {
                header("x-api-key", BuildKonfig.API_KEY)
                contentType(ContentType.Application.Json)
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        sessionStorage
                            .observeAuthInfo()
                            .firstOrNull()
                            ?.let {
                                BearerTokens(
                                    it.accessToken,
                                    it.refreshToken
                                )
                            }
                    }
                    refreshTokens {
                        if (response.request.url.encodedPath.contains("auth/")) {
                            return@refreshTokens null
                        }

                        val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
                        if (authInfo?.refreshToken.isNullOrBlank()) {
                            sessionStorage.set(null)
                            return@refreshTokens null
                        }

                        var bearerTokens: BearerTokens? = null
                        client.post<RefreshRequest, AuthInfoSerializable>(
                            route = "/auth/refresh",
                            body = RefreshRequest(
                                refreshToken = authInfo.refreshToken
                            ),
                            builder = {
                                markAsRefreshTokenRequest()
                            }
                        ).onSuccess { newAuthInfo ->
                            sessionStorage.set(newAuthInfo.toAuthInfo())
                            bearerTokens = BearerTokens(
                                newAuthInfo.accessToken,
                                newAuthInfo.refreshToken
                            )
                        }.onFailure { error ->
                            sessionStorage.set(null)
                        }
                        bearerTokens
                    }
                }
            }
        }
    }
}