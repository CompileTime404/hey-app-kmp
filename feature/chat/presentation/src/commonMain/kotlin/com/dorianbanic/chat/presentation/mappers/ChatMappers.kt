package com.dorianbanic.chat.presentation.mappers

import com.dorianbanic.chat.domain.models.Chat
import com.dorianbanic.chat.presentation.model.ChatUi

fun Chat.toUi(localParticipantId: String): ChatUi {
    val (local, other) = participants.partition { it.userId == localParticipantId }
    return ChatUi(
        id = id,
        localParticipant = local.first().toChatParticipantUi(),
        otherParticipants = other.map { it.toChatParticipantUi() },
        lastMessage = lastMessage,
        lastMessageSenderUsername = lastMessageSenderUsername
    )
}