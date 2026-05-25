package com.dorianbanic.chat.presentation.chat_detail

import com.dorianbanic.core.presentation.util.UiText

sealed interface ChatDetailEvent {
    data object OnChatLeft: ChatDetailEvent
    data class OnError(val error: UiText): ChatDetailEvent
    data object OnNewMessage: ChatDetailEvent
}