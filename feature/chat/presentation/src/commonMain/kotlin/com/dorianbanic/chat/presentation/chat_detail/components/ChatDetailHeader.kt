package com.dorianbanic.chat.presentation.chat_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dorianbanic.chat.domain.models.ChatMessage
import com.dorianbanic.chat.presentation.components.ChatHeader
import com.dorianbanic.chat.presentation.components.ChatItemHeaderRow
import com.dorianbanic.chat.presentation.model.ChatUi
import com.dorianbanic.core.desingsystem.components.avatar.ChatParticipantUi
import com.dorianbanic.core.desingsystem.components.buttons.HeyappIconButton
import com.dorianbanic.core.desingsystem.components.dropdown.DropDownItem
import com.dorianbanic.core.desingsystem.components.dropdown.HeyappDropDownMenu
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.desingsystem.theme.extended
import heyapp.core.desingsystem.generated.resources.arrow_left_icon
import heyapp.core.desingsystem.generated.resources.dots_icon
import heyapp.core.desingsystem.generated.resources.log_out_icon
import heyapp.core.desingsystem.generated.resources.users_icon
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.chat_members
import heyapp.feature.chat.presentation.generated.resources.leave_chat
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import heyapp.core.desingsystem.generated.resources.Res as DesignSystemRes

@Composable
fun ChatDetailHeader(
    chatUi: ChatUi?,
    isDetailPresent: Boolean,
    isChatOptionsDropDownOpen: Boolean,
    onChatOptionsClick: () -> Unit,
    onDismissChatOptions: () -> Unit,
    onManageChatClick: () -> Unit,
    onLeaveChatClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface
            ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isDetailPresent) {
            HeyappIconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.arrow_left_icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.extended.textSecondary
                )
            }
        }
        if (chatUi != null) {
            val isGroupChat = chatUi.otherParticipants.size > 1
            ChatItemHeaderRow(
                chat = chatUi,
                isGroupChat = isGroupChat,
                modifier = Modifier
                    .weight(1f)
                    .clickable() {
                        onManageChatClick()
                    }
            )
        } else {
            Spacer(Modifier.weight(1f))
        }
        Box() {
            HeyappIconButton(
                onClick = onChatOptionsClick,
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.dots_icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.extended.textSecondary
                )
            }
            HeyappDropDownMenu(
                isOpen = isChatOptionsDropDownOpen,
                onDismiss = onDismissChatOptions,
                items = listOf(
                    DropDownItem(
                        title = stringResource(Res.string.chat_members),
                        icon = vectorResource(DesignSystemRes.drawable.users_icon),
                        contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                        onClick = onManageChatClick
                    ),
                    DropDownItem(
                        title = stringResource(Res.string.leave_chat),
                        icon = vectorResource(DesignSystemRes.drawable.log_out_icon),
                        contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                        onClick = onLeaveChatClick
                    ),
                )
            )
        }
    }
}