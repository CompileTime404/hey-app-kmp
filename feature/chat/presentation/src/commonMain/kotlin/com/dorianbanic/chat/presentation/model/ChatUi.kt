package com.dorianbanic.chat.presentation.model

import com.dorianbanic.chat.domain.models.ChatMessage
import com.dorianbanic.core.desingsystem.components.avatar.ChatParticipantUi

data class ChatUi(
    val id: String,
    val localParticipant: ChatParticipantUi,
    val otherParticipants: List<ChatParticipantUi>,
    val lastMessage: ChatMessage?,
    val lastMessageSenderUsername: String?
)
