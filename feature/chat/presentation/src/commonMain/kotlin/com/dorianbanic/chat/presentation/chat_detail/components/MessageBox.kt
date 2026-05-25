package com.dorianbanic.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.dorianbanic.chat.domain.models.ConnectionState
import com.dorianbanic.chat.presentation.util.toUiText
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButton
import com.dorianbanic.core.desingsystem.components.textfields.HeyappMultiLineTextField
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.desingsystem.theme.extended
import heyapp.core.desingsystem.generated.resources.cloud_off_icon
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.send
import heyapp.feature.chat.presentation.generated.resources.send_a_message
import heyapp.core.desingsystem.generated.resources.Res as DesingSystemR
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MessageBox(
    messageTextFieldState: TextFieldState,
    isSendButtonEnabled: Boolean,
    connectionState: ConnectionState,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected = connectionState == ConnectionState.CONNECTED
    HeyappMultiLineTextField(
        state = messageTextFieldState,
        modifier = modifier
            .onPreviewKeyEvent { keyEvent ->
                val isModifierKeyPressed = keyEvent.isMetaPressed || keyEvent.isCtrlPressed
                val isSendShortcutPressed = isModifierKeyPressed
                        && keyEvent.key == Key.Enter
                        && keyEvent.type == KeyEventType.KeyDown

                if(isSendShortcutPressed) {
                    onSendClick()
                    true
                } else false
            },
        placeholder = stringResource(Res.string.send_a_message),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send
        ),
        onKeyboardAction = onSendClick,
        bottomContent = {
            Spacer(Modifier.weight(1f))
            if (!isConnected) {
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = vectorResource(DesingSystemR.drawable.cloud_off_icon),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.extended.textDisabled
                    )
                    Text(
                        text = connectionState.toUiText().asString(),
                        color = MaterialTheme.colorScheme.extended.textDisabled,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            HeyappButton(
                text = stringResource(Res.string.send),
                onClick = onSendClick,
                enabled = isSendButtonEnabled && isConnected
            )
        }
    )
}

@Composable
@Preview
fun MessageBoxPreview() {
    HeyAppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            MessageBox(
                messageTextFieldState = rememberTextFieldState(),
                isSendButtonEnabled = true,
                connectionState = ConnectionState.CONNECTED,
                onSendClick = {},
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}