package com.dorianbanic.chat.domain.chat

import com.dorianbanic.chat.domain.models.ChatMessage
import com.dorianbanic.chat.domain.models.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ChatConnectionClient {
    val chatMessages: Flow<ChatMessage>
    val connectionState: StateFlow<ConnectionState>

}