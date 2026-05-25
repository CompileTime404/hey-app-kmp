package com.dorianbanic.chat.data.message

import com.dorianbanic.chat.data.dto.websocket.OutgoingWebSocketDto
import com.dorianbanic.chat.data.dto.websocket.WebSocketMessageDto
import com.dorianbanic.chat.data.mappers.toDomain
import com.dorianbanic.chat.data.mappers.toEntity
import com.dorianbanic.chat.data.mappers.toNewMessage
import com.dorianbanic.chat.data.mappers.toWebSocketDto
import com.dorianbanic.chat.data.network.KtorWebSocketConnector
import com.dorianbanic.chat.database.HeyappChatDatabase
import com.dorianbanic.chat.domain.message.MessageRepository
import com.dorianbanic.chat.domain.models.ChatMessage
import com.dorianbanic.chat.domain.models.ChatMessageDeliveryStatus
import com.dorianbanic.chat.domain.models.MessageWithSender
import com.dorianbanic.chat.domain.models.OutgoingNewMessage
import com.dorianbanic.core.data.database.safeDatabaseUpdate
import com.dorianbanic.core.domain.auth.SessionStorage
import com.dorianbanic.core.domain.logging.HeyappLogger
import com.dorianbanic.core.domain.util.DataError
import com.dorianbanic.core.domain.util.EmptyResult
import com.dorianbanic.core.domain.util.Result
import com.dorianbanic.core.domain.util.onFailure
import com.dorianbanic.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class OfflineFirstMessageRepository(
    private val database: HeyappChatDatabase,
    private val chatMessageService: KtorChatMessageService,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val webSocketConnector: KtorWebSocketConnector,
    private val applicationScope: CoroutineScope
) : MessageRepository {

    override suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val dto = message.toWebSocketDto()

            val localUser = sessionStorage.observeAuthInfo().first()?.user
                ?: return Result.Failure(DataError.Local.NOT_FOUND)
            val entity = dto.toEntity(
                senderId = localUser.id,
                deliveryStatus = ChatMessageDeliveryStatus.SENDING
            )

            database.chatMessageDao.upsertMessage(entity)

            return webSocketConnector
                .sendMessage(dto.toJsonPayload())
                .onFailure { error ->
                    applicationScope.launch {
                        database.chatMessageDao.updateDeliveryStatus(
                            messageId = entity.messageId,
                            timestamp = Clock.System.now().toEpochMilliseconds(),
                            status = ChatMessageDeliveryStatus.FAILED.name
                        )
                    }.join()
                }
        }
    }

    override suspend fun retryMessage(messageId: String): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val message = database.chatMessageDao.getMessageById(messageId)
                ?: return Result.Failure(DataError.Local.NOT_FOUND)

            database.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                timestamp = Clock.System.now().toEpochMilliseconds(),
                status = ChatMessageDeliveryStatus.SENDING.name,
            )

            val outgoingNewMessage = OutgoingWebSocketDto.NewMessage(
                chatId = message.chatId,
                messageId = messageId,
                content = message.content,
            )

            return webSocketConnector
                .sendMessage(outgoingNewMessage.toJsonPayload())
                .onFailure {
                    applicationScope.launch {
                        database.chatMessageDao.updateDeliveryStatus(
                            messageId = messageId,
                            timestamp = Clock.System.now().toEpochMilliseconds(),
                            status = ChatMessageDeliveryStatus.FAILED.name
                        )
                    }.join()
                }
        }
    }

    override suspend fun deleteMessage(messageId: String): EmptyResult<DataError.Remote> {
        return chatMessageService
            .deleteMessage(messageId)
            .onSuccess {
                applicationScope.launch {
                    database.chatMessageDao.deleteMessageById(messageId)
                }.join()
            }
    }

    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return safeDatabaseUpdate {
            database.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                status = status.name,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError> {
        return chatMessageService
            .fetchMessages(chatId, before)
            .onSuccess { messages ->
                return safeDatabaseUpdate {
                    database.chatMessageDao.upsertMessagesAndSyncIfNecessary(
                        chatId = chatId,
                        serverMessages = messages.map { it.toEntity() },
                        pageSize = ChatMessageConstants.PAGE_SIZE,
                        shouldSync = before == null
                    )
                    messages
                }
            }
    }

    override fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>> {
        return database
            .chatMessageDao
            .getMessagesByChatId(chatId)
            .map { messages ->
                messages.map { it.toDomain() }
            }
    }

    private fun OutgoingWebSocketDto.NewMessage.toJsonPayload(): String {
        val webSocketMessage = WebSocketMessageDto(
            type = type.name,
            payload = json.encodeToString(this)
        )
        return json.encodeToString(webSocketMessage)

    }
}