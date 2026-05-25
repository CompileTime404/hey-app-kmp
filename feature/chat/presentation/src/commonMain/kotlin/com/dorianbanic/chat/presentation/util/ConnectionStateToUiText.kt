package com.dorianbanic.chat.presentation.util

import com.dorianbanic.chat.domain.models.ConnectionState
import com.dorianbanic.core.presentation.util.UiText
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.network_error
import heyapp.feature.chat.presentation.generated.resources.offline
import heyapp.feature.chat.presentation.generated.resources.online
import heyapp.feature.chat.presentation.generated.resources.reconnecting
import heyapp.feature.chat.presentation.generated.resources.unknown_error

fun ConnectionState.toUiText(): UiText {
    val resource = when(this) {
        ConnectionState.DISCONNECTED -> Res.string.offline
        ConnectionState.CONNECTING -> Res.string.reconnecting
        ConnectionState.CONNECTED -> Res.string.online
        ConnectionState.ERROR_NETWORK -> Res.string.network_error
        ConnectionState.ERROR_UNKNOWN -> Res.string.unknown_error
    }
    return UiText.Resource(resource)
}