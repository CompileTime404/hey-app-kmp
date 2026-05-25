package com.dorianbanic.chat.presentation.chat_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.dorianbanic.chat.domain.models.Chat
import com.dorianbanic.chat.domain.models.ChatMessage
import com.dorianbanic.chat.presentation.components.ChatItemHeaderRow
import com.dorianbanic.chat.presentation.mappers.toChatParticipantUi
import com.dorianbanic.chat.presentation.model.ChatUi
import com.dorianbanic.core.desingsystem.components.avatar.ChatParticipantUi
import com.dorianbanic.core.desingsystem.components.avatar.HeyappStackedAvatars
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.desingsystem.theme.extended
import com.dorianbanic.core.desingsystem.theme.titleXSmall
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.group_chat
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@Composable
fun ChatListItemUi(
    chat: ChatUi,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val isGroupChat = remember(chat) {
        chat.otherParticipants.size > 1
    }
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(
                color = if(isSelected) {
                    MaterialTheme.colorScheme.surface
                } else {
                    MaterialTheme.colorScheme.extended.surfaceLower
                }
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ChatItemHeaderRow(
                chat = chat,
                isGroupChat = isGroupChat,
                modifier = Modifier
                    .fillMaxWidth()
            )
            if (chat.lastMessage != null) {
                val previewMessage = buildAnnotatedString { 
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.extended.textSecondary,
                        )
                    ) {
                        if (chat.lastMessageSenderUsername != null) {
                            append(chat.lastMessageSenderUsername + ": ")
                        }
                    }
                    append(chat.lastMessage.content)
                }
                Text(
                    text = previewMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Box(
            modifier = Modifier
                .alpha(if (isSelected) 1f else 0f)
                .background(MaterialTheme.colorScheme.primary)
                .width(4.dp)
                .fillMaxHeight()
        )
    }
}