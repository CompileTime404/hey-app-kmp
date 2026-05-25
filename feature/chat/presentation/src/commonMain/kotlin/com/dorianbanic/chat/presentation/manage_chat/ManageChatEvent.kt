package com.dorianbanic.chat.presentation.manage_chat

sealed interface ManageChatEvent {
    data object OnMembersAdded: ManageChatEvent
}