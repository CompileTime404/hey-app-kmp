package com.dorianbanic.chat.presentation.chat_detail

import androidx.compose.foundation.text.input.TextFieldState
import com.dorianbanic.chat.domain.models.ConnectionState
import com.dorianbanic.chat.presentation.model.ChatUi
import com.dorianbanic.chat.presentation.model.MessageUi
import com.dorianbanic.core.presentation.util.UiText

data class ChatDetailState(
    val chatUi: ChatUi? = null,
    val isLoading: Boolean = false,
    val messages: List<MessageUi> = emptyList(),
    val error: UiText? = null,
    val messageTextFieldState: TextFieldState = TextFieldState(),
    val canSendMessage: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val paginationError: UiText? = null,
    val endReached: Boolean = false,
    val bannerState: BannerState = BannerState(),
    val messageWithOpenMenu: MessageUi.LocalUserMessage? = null,
    val isChatOptionsOpen: Boolean = false,
    val showLeaveChatConfirmationDialog: Boolean = false,
    val isNearBottom: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)

data class BannerState(
    val formattedDate: UiText? = null,
    val isVisible: Boolean = false
)