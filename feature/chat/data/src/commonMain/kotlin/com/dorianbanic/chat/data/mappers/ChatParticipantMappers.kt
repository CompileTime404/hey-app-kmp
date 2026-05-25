package com.dorianbanic.chat.data.mappers

import com.dorianbanic.chat.data.dto.ChatParticipantDto
import com.dorianbanic.chat.database.entities.ChatParticipantEntity
import com.dorianbanic.chat.domain.models.ChatParticipant

fun ChatParticipantDto.toChatParticipant(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}

fun ChatParticipantEntity.toDomain(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}

fun ChatParticipant.toEntity(): ChatParticipantEntity {
    return ChatParticipantEntity(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}