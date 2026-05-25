package com.dorianbanic.chat.domain.chat

import com.dorianbanic.chat.domain.models.Chat
import com.dorianbanic.chat.domain.models.ChatInfo
import com.dorianbanic.chat.domain.models.ChatParticipant
import com.dorianbanic.core.domain.util.DataError
import com.dorianbanic.core.domain.util.EmptyResult
import com.dorianbanic.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(): Flow<List<Chat>>
    fun getChatInfoById(chatId: String): Flow<ChatInfo>
    fun getActiveParticipantsByChatId(chatId: String): Flow<List<ChatParticipant>>
    suspend fun fetchChats(): Result<List<Chat>, DataError.Remote>
    suspend fun fetchChatById(chatId: String): EmptyResult<DataError.Remote>
    suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote>
    suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote>
    suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote>
    suspend fun deleteAllChats()
}