package com.dorianbanic.chat.data.participant

import com.dorianbanic.chat.data.dto.ChatParticipantDto
import com.dorianbanic.chat.data.dto.request.ConfirmProfilePictureRequest
import com.dorianbanic.chat.data.dto.response.ProfilePictureUploadUrlsResponse
import com.dorianbanic.chat.data.mappers.toChatParticipant
import com.dorianbanic.chat.data.mappers.toDomain
import com.dorianbanic.chat.domain.participant.ChatParticipantService
import com.dorianbanic.chat.domain.models.ChatParticipant
import com.dorianbanic.chat.domain.models.ProfilePictureUploadUrls
import com.dorianbanic.core.data.networking.constructRoute
import com.dorianbanic.core.data.networking.delete
import com.dorianbanic.core.data.networking.get
import com.dorianbanic.core.data.networking.post
import com.dorianbanic.core.data.networking.put
import com.dorianbanic.core.data.networking.safeCall
import com.dorianbanic.core.domain.util.DataError
import com.dorianbanic.core.domain.util.EmptyResult
import com.dorianbanic.core.domain.util.Result
import com.dorianbanic.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import kotlin.collections.component1
import kotlin.collections.component2

class KtorChatParticipantService(
    private val httpClient: HttpClient
): ChatParticipantService {
    override suspend fun searchParticipant(query: String): Result<ChatParticipant, DataError.Remote> {
        return httpClient.get<ChatParticipantDto>(
            route = "/participants",
            queryParams = mapOf(
                "query" to query
            )
        ).map { it.toChatParticipant() }
    }

    override suspend fun getLocalParticipant(): Result<ChatParticipant, DataError.Remote> {
        return httpClient.get<ChatParticipantDto>(
            route = "/participants",
        ).map { it.toChatParticipant() }
    }

    override suspend fun getProfilePictureUploadUrl(mimeType: String): Result<ProfilePictureUploadUrls, DataError.Remote> {
        return httpClient.post<Unit, ProfilePictureUploadUrlsResponse>(
            route = "/participants/profile-picture-upload",
            queryParams = mapOf(
                "mimeType" to mimeType
            ),
            body = Unit
        ).map { it.toDomain() }
    }

    override suspend fun uploadProfilePicture(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote> {
        return safeCall {
            httpClient.put {
                url(uploadUrl)
                headers.forEach { (key, value) ->
                    header(key, value)
                }
                setBody(imageBytes)
            }
        }
    }

    override suspend fun confirmProfilePictureUpload(publicUrl: String): EmptyResult<DataError.Remote> {
        return httpClient.post<ConfirmProfilePictureRequest, Unit>(
            route = "/participants/confirm-profile-picture",
            body = ConfirmProfilePictureRequest(publicUrl)
        )
    }

    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/participants/profile-picture"
        )
    }
}