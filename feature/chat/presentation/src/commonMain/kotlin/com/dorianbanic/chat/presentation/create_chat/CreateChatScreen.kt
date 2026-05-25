package com.dorianbanic.chat.presentation.create_chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dorianbanic.chat.domain.models.Chat
import com.dorianbanic.chat.presentation.components.manage_chat.ManageChatAction
import com.dorianbanic.chat.presentation.components.manage_chat.ManageChatScreen
import com.dorianbanic.core.desingsystem.components.dialogs.HeyappAdaptiveDialogSheetLayout
import com.dorianbanic.core.presentation.util.ObserveAsEvents
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.create_chat
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateChatRoot(
    onDismiss: () -> Unit,
    onChatCreated: (Chat) -> Unit,
    viewModel: CreateChatViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CreateChatEvent.OnChatCreated -> onChatCreated(event.chat)
        }
    }

    HeyappAdaptiveDialogSheetLayout(
        onDismiss = onDismiss
    ) {
        ManageChatScreen(
            headerText = stringResource(Res.string.create_chat),
            primaryButtonText = stringResource(Res.string.create_chat),
            state = state,
            onAction = { action ->
                when(action) {
                    ManageChatAction.OnDismissDialog -> onDismiss()
                    else -> Unit
                }
                viewModel.onAction(action)
            }
        )
    }
}