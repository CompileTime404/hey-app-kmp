package com.dorianbanic.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButton
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButtonStyle
import com.dorianbanic.core.desingsystem.components.textfields.HeyappTextField
import com.dorianbanic.core.presentation.util.UiText
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.add
import heyapp.feature.chat.presentation.generated.resources.email_or_username
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatParticipantSearchTextSection(
    queryState: TextFieldState,
    onAddClick: () -> Unit,
    isSearchEnabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    error: UiText? = null,
    onFocusChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeyappTextField(
            state = queryState,
            modifier = Modifier
                .weight(1f),
            placeholder = stringResource(Res.string.email_or_username),
            title = null,
            supportingText = error?.asString(),
            isError = error != null,
            keyboardType = KeyboardType.Email,
            onFocusChanged = onFocusChanged
        )
        HeyappButton(
            text = stringResource(Res.string.add),
            onClick = onAddClick,
            enabled = isSearchEnabled && !isLoading,
            style = HeyappButtonStyle.SECONDARY,
            isLoading = isLoading
        )
    }
}
