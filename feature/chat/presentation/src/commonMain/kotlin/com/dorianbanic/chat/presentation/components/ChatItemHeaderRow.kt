package com.dorianbanic.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dorianbanic.chat.presentation.model.ChatUi
import com.dorianbanic.core.desingsystem.components.avatar.HeyappStackedAvatars
import com.dorianbanic.core.desingsystem.theme.extended
import com.dorianbanic.core.desingsystem.theme.titleXSmall
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.group_chat
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatItemHeaderRow(
    chat: ChatUi,
    isGroupChat: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        if (chat.otherParticipants.isNotEmpty()) {
            HeyappStackedAvatars(
                avatars = chat.otherParticipants
            )
        }
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (!isGroupChat) {
                    chat.otherParticipants.firstOrNull()?.username ?: "Only you"
                } else {
                    stringResource(Res.string.group_chat)
                },
                style = MaterialTheme.typography.titleXSmall,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            if (isGroupChat) {
                val formattedUsernames = remember(chat.otherParticipants) {
                    "You, " + chat.otherParticipants.joinToString() {
                        it.username
                    }
                }
                Text(
                    text = formattedUsernames,
                    color = MaterialTheme.colorScheme.extended.textPlaceholder,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}