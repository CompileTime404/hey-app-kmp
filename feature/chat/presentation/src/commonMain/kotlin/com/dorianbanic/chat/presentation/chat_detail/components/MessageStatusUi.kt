package com.dorianbanic.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dorianbanic.chat.domain.models.ChatMessageDeliveryStatus
import com.dorianbanic.core.desingsystem.theme.extended
import com.dorianbanic.core.desingsystem.theme.labelXSmall
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.check_icon
import heyapp.feature.chat.presentation.generated.resources.failed
import heyapp.feature.chat.presentation.generated.resources.loading_icon
import heyapp.feature.chat.presentation.generated.resources.sending
import heyapp.feature.chat.presentation.generated.resources.sent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MessageStatus(
    status: ChatMessageDeliveryStatus,
    modifier: Modifier = Modifier
) {
    val (text, icon, color) = when(status) {
        ChatMessageDeliveryStatus.SENDING -> Triple(
            stringResource(Res.string.sending),
            vectorResource(Res.drawable.loading_icon),
            MaterialTheme.colorScheme.extended.textDisabled
        )
        ChatMessageDeliveryStatus.SENT -> Triple(
            stringResource(Res.string.sent),
            vectorResource(Res.drawable.check_icon),
            MaterialTheme.colorScheme.extended.textTertiary
        )
        ChatMessageDeliveryStatus.FAILED -> Triple(
            stringResource(Res.string.failed),
            Icons.Default.Close,
            MaterialTheme.colorScheme.error
        )
    }
    Row(
        modifier = modifier,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelXSmall,
            color = color
        )
    }
}