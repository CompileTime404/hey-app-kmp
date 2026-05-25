package com.dorianbanic.core.data.auth

import com.dorianbanic.core.data.dto.AuthInfoSerializable
import com.dorianbanic.core.data.dto.request.ChangePasswordRequest
import com.dorianbanic.core.data.dto.request.EmailRequest
import com.dorianbanic.core.data.dto.request.LoginRequest
import com.dorianbanic.core.data.dto.request.RefreshRequest
import com.dorianbanic.core.data.dto.request.RegisterRequest
import com.dorianbanic.core.data.dto.request.ResetPasswordRequest
import com.dorianbanic.core.data.mappers.toAuthInfo
import com.dorianbanic.core.data.networking.get
import com.dorianbanic.core.data.networking.post
import com.dorianbanic.core.domain.auth.AuthInfo
import com.dorianbanic.core.domain.auth.AuthService
import com.dorianbanic.core.domain.util.DataError
import com.dorianbanic.core.domain.util.EmptyResult
import com.dorianbanic.core.domain.util.Result
import com.dorianbanic.core.domain.util.asEmptyResult
import com.dorianbanic.core.domain.util.map
import com.dorianbanic.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider


class KtorAuthService(
    private val httpClient: HttpClient
): AuthService {

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/register",
            body = RegisterRequest(
                email = email,
                username = username,
                password = password
            )
        )
    }

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/resend-verification",
            body = EmailRequest(
                email = email
            )
        )
    }

    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        return httpClient.get(
            route = "/auth/verify",
            queryParams = mapOf("token" to token)
        )
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote> {
        httpClient.authProvider<BearerAuthProvider>()?.clearToken()
        return httpClient.post<LoginRequest, AuthInfoSerializable>(
            route = "/auth/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        ).onSuccess {
            httpClient.authProvider<BearerAuthProvider>()?.clearToken()
        }.map { authInfoSerializable ->
            authInfoSerializable.toAuthInfo()
        }
    }

    override suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post<EmailRequest, Unit>(
            route = "/auth/forgot-password",
            body = EmailRequest(
                email = email
            )
        )
    }

    override suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/reset-password",
            body = ResetPasswordRequest(
                newPassword = newPassword,
                token = token
            )
        )
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/change-password",
            body = ChangePasswordRequest(
                oldPassword = currentPassword,
                newPassword = newPassword
            )
        )
    }

    override suspend fun logout(refreshToken: String): EmptyResult<DataError.Remote> {
        return httpClient.post<RefreshRequest, Unit>(
            route = "/auth/logout",
            body = RefreshRequest(
                refreshToken = refreshToken
            )
        ).onSuccess {
            httpClient.authProvider<BearerAuthProvider>()?.clearToken()
        }
    }
}