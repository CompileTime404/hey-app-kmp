package com.dorianbanic.chat.presentation.components.manage_chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.dorianbanic.chat.presentation.components.ChatParticipantSearchTextSection
import com.dorianbanic.chat.presentation.components.ChatParticipantsSelectionSection
import com.dorianbanic.chat.presentation.components.ManageChatButtonSection
import com.dorianbanic.chat.presentation.components.ManageChatHeaderRow
import com.dorianbanic.core.desingsystem.components.brand.HeyappHorizontalDivider
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButton
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButtonStyle
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.presentation.util.DeviceConfiguration
import com.dorianbanic.core.presentation.util.clearFocusOnTap
import com.dorianbanic.core.presentation.util.currentDeviceConfiguration
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.cancel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ManageChatScreen(
    headerText: String,
    primaryButtonText: String,
    state: ManageChatState,
    onAction: (ManageChatAction) -> Unit,
) {
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val isKeyboardVisible = imeHeight > 0
    val configuration = currentDeviceConfiguration()

    val shouldHideHeader = configuration == DeviceConfiguration.MOBILE_LANDSCAPE
            && (isTextFieldFocused || isKeyboardVisible)

    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxWidth()
            .wrapContentHeight()
            .imePadding()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
    ) {
        AnimatedVisibility(
            visible = !shouldHideHeader
        ) {
            Column {
                ManageChatHeaderRow(
                    title = headerText,
                    onCloseClick = {
                        onAction(ManageChatAction.OnDismissDialog)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                HeyappHorizontalDivider()
            }
        }
        ChatParticipantSearchTextSection(
            queryState = state.queryTextState,
            onAddClick = {
                onAction(ManageChatAction.OnAddClick)
            },
            isSearchEnabled = state.canAddParticipant,
            isLoading = state.isSearching,
            modifier = Modifier
                .fillMaxWidth(),
            error = state.searchError,
            onFocusChanged = {
                isTextFieldFocused = it
            }
        )
        HeyappHorizontalDivider()
        ChatParticipantsSelectionSection(
            existingParticipants = state.existingChatParticipants,
            selectedParticipants = state.selectedChatParticipants,
            searchResult = state.currentSearchResult,
            modifier = Modifier
                .fillMaxWidth()
        )
        HeyappHorizontalDivider()
        ManageChatButtonSection(
            primaryButton = {
                HeyappButton(
                    text = primaryButtonText,
                    onClick = {
                        onAction(ManageChatAction.OnPrimaryActionClick)
                    },
                    enabled = state.selectedChatParticipants.isNotEmpty(),
                    isLoading = state.isSubmitting
                )
            },
            secondaryButton = {
                HeyappButton(
                    text = stringResource(Res.string.cancel),
                    onClick = {
                        onAction(ManageChatAction.OnDismissDialog)
                    },
                    style = HeyappButtonStyle.SECONDARY
                )
            },
            error = state.submitError?.asString(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun Preview() {
    HeyAppTheme {
        ManageChatScreen(
            headerText = "Chat members",
            primaryButtonText = "Create",
            state = ManageChatState(),
            onAction = {}
        )
    }
}