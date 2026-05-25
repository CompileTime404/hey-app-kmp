package com.dorianbanic.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dorianbanic.chat.presentation.model.MessageUi
import com.dorianbanic.chat.presentation.util.getChatBubbleColorForUser
import com.dorianbanic.core.desingsystem.theme.extended

@Composable
fun MessageListItemUi(
    messageUi: MessageUi,
    messageWithOpenMenu: MessageUi.LocalUserMessage?,
    onMessageLongClick: (MessageUi.LocalUserMessage) -> Unit,
    onDismissMessageMenu: () -> Unit,
    onDeleteClick: (MessageUi.LocalUserMessage) -> Unit,
    onRetryClick: (MessageUi.LocalUserMessage) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        when (messageUi) {
            is MessageUi.DateSeparator -> {
                DateSeparatorUi(
                    date = messageUi.date.asString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is MessageUi.LocalUserMessage -> {
                LocalUserMessage(
                    message = messageUi,
                    modifier = Modifier.fillMaxWidth(),
                    messageWithOpenMenu = messageWithOpenMenu,
                    onMessageLongClick = { onMessageLongClick(messageUi) },
                    onDismissMessageMenu = onDismissMessageMenu,
                    onDeleteClick = { onDeleteClick(messageUi) },
                    onRetryClick = { onRetryClick(messageUi) }
                )
            }
            is MessageUi.OtherUserMessage -> {
                OtherUserMessage(
                    message = messageUi,
                    color = getChatBubbleColorForUser(messageUi.sender.id)
                )
            }
        }
    }
}

@Composable
private fun DateSeparatorUi(
    date: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(Modifier.weight(1f))
        Text(
            text = date,
            modifier = Modifier.padding(horizontal = 40.dp),
            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
            color = androidx.compose.material3.MaterialTheme.colorScheme.extended.textPlaceholder
        )
        HorizontalDivider(Modifier.weight(1f))
    }
}
