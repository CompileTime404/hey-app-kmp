package com.dorianbanic.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dorianbanic.chat.domain.models.ChatMessageDeliveryStatus
import com.dorianbanic.chat.presentation.model.MessageUi
import com.dorianbanic.core.desingsystem.components.chat.HeyappChatBubble
import com.dorianbanic.core.desingsystem.components.chat.TrianglePosition
import com.dorianbanic.core.desingsystem.components.dropdown.DropDownItem
import com.dorianbanic.core.desingsystem.components.dropdown.HeyappDropDownMenu
import com.dorianbanic.core.desingsystem.theme.extended
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.delete_for_everyone
import heyapp.feature.chat.presentation.generated.resources.reload_icon
import heyapp.feature.chat.presentation.generated.resources.retry
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LocalUserMessage(
    message: MessageUi.LocalUserMessage,
    messageWithOpenMenu: MessageUi.LocalUserMessage?,
    onMessageLongClick: () -> Unit,
    onDismissMessageMenu: () -> Unit,
    onDeleteClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        verticalAlignment = Alignment.Bottom
    ) {
        Box {
            HeyappChatBubble(
                messageContent = message.content,
                sender = "You",
                formattedDateTime = message.formattedSentTime.asString(),
                trianglePosition = TrianglePosition.RIGHT,
                messageStatus = {
                    MessageStatus(
                        status = message.deliveryStatus,
                    )
                },
                onLongClick = {
                    onMessageLongClick()
                }
            )

            HeyappDropDownMenu(
                isOpen = messageWithOpenMenu?.id == message.id,
                onDismiss = onDismissMessageMenu,
                items = listOf(
                    DropDownItem(
                        title = stringResource(Res.string.delete_for_everyone),
                        icon = Icons.Default.Delete,
                        contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                        onClick = onDeleteClick
                    )
                )
            )
        }

        if(message.deliveryStatus == ChatMessageDeliveryStatus.FAILED) {
            IconButton(
                onClick = onRetryClick
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.reload_icon),
                    contentDescription = stringResource(Res.string.retry),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}