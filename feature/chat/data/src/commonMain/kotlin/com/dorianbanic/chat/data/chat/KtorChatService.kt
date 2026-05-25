package com.dorianbanic.chat.data.chat

import com.dorianbanic.chat.data.dto.ChatDto
import com.dorianbanic.chat.data.dto.request.CreateChatRequest
import com.dorianbanic.chat.data.dto.request.ParticipantsRequest
import com.dorianbanic.chat.data.mappers.toDomain
import com.dorianbanic.chat.domain.chat.ChatService
import com.dorianbanic.chat.domain.models.Chat
import com.dorianbanic.core.data.networking.delete
import com.dorianbanic.core.data.networking.get
import com.dorianbanic.core.data.networking.post
import com.dorianbanic.core.domain.util.DataError
import com.dorianbanic.core.domain.util.EmptyResult
import com.dorianbanic.core.domain.util.Result
import com.dorianbanic.core.domain.util.asEmptyResult
import com.dorianbanic.core.domain.util.map
import io.ktor.client.HttpClient

class KtorChatService(
    private val httpClient: HttpClient
) : ChatService {
    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return httpClient.post<CreateChatRequest, ChatDto>(
            route = "/chat",
            body = CreateChatRequest(
                otherUserIds = otherUserIds
            )
        ).map { it.toDomain() }
    }

    override suspend fun getChats(): Result<List<Chat>, DataError.Remote> {
        return httpClient.get<List<ChatDto>>(
            route = "/chat"
        ).map { chatDtos ->
            chatDtos.map { it.toDomain() }
        }
    }

    override suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote> {
        return httpClient.get<ChatDto>(
            route = "/chat/$chatId"
        ).map { it.toDomain() }
    }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return httpClient.delete<Unit>(
            route = "/chat/$chatId/leave"
        ).asEmptyResult()
    }

    override suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote> {
        return httpClient.post<ParticipantsRequest, ChatDto>(
            route = "/chat/$chatId/add",
            body = ParticipantsRequest(
                userIds = userIds
            )
        ).map { it.toDomain() }
    }
}