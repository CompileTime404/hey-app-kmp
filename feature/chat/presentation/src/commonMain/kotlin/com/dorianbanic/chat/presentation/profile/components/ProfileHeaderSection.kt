package com.dorianbanic.chat.presentation.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButton
import com.dorianbanic.core.desingsystem.components.buttons.HeyappIconButton
import com.dorianbanic.core.desingsystem.theme.extended
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.profile_settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileHeaderSection(
    username: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary
            )
            Text(
                text = stringResource(Res.string.profile_settings),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.extended.textSecondary
            )
        }
        HeyappIconButton(
            onClick = onCloseClick
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.extended.textSecondary
            )
        }
    }
}