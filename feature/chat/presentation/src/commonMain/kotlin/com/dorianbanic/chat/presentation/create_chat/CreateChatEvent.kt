package com.dorianbanic.chat.presentation.create_chat

import com.dorianbanic.chat.domain.models.Chat

sealed interface CreateChatEvent {
    data class OnChatCreated(val chat: Chat): CreateChatEvent
}