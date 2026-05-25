package com.dorianbanic.chat.presentation.mappers

import com.dorianbanic.chat.domain.models.ChatParticipant
import com.dorianbanic.core.desingsystem.components.avatar.ChatParticipantUi
import com.dorianbanic.core.domain.auth.User

fun ChatParticipant.toChatParticipantUi(): ChatParticipantUi {
    return ChatParticipantUi(
        id = userId,
        username = username,
        initials = initials,
        imageUrl = profilePictureUrl
    )
}

fun User.toUi(): ChatParticipantUi {
    return ChatParticipantUi(
        id = id,
        username = username,
        initials = username.take(2).uppercase(),
        imageUrl = profilePictureUrl
    )
}