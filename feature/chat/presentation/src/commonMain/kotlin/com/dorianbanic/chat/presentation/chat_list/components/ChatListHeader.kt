package com.dorianbanic.chat.presentation.chat_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dorianbanic.chat.presentation.components.ChatHeader
import com.dorianbanic.core.desingsystem.components.avatar.ChatParticipantUi
import com.dorianbanic.core.desingsystem.components.avatar.HeyappAvatarPhoto
import com.dorianbanic.core.desingsystem.components.dropdown.DropDownItem
import com.dorianbanic.core.desingsystem.components.dropdown.HeyappDropDownMenu
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.desingsystem.theme.extended
import heyapp.core.desingsystem.generated.resources.log_out_icon
import heyapp.core.desingsystem.generated.resources.users_icon
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.logout
import heyapp.feature.chat.presentation.generated.resources.profile_settings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import heyapp.core.desingsystem.generated.resources.Res as DesignSystemRes

@Composable
fun ChatListHeader(
    localParticipant: ChatParticipantUi?,
    isUserMenuOpen: Boolean,
    onUserAvatarClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChatHeader(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "HeyApp",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary
            )
            Spacer(Modifier.weight(1f))
            ProfileAvatarSection(
                localParticipant = localParticipant,
                isUserMenuOpen = isUserMenuOpen,
                onClick = onUserAvatarClick,
                onDismissMenu = onDismissMenu,
                onProfileSettingsClick = onProfileSettingsClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}

@Composable
fun ProfileAvatarSection(
    localParticipant: ChatParticipantUi?,
    isUserMenuOpen: Boolean,
    onClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        if (localParticipant != null) {
            HeyappAvatarPhoto(
                displayText = localParticipant.initials,
                imageUrl = localParticipant.imageUrl,
                onClick = onClick
            )
        }

        HeyappDropDownMenu(
            isOpen = isUserMenuOpen,
            onDismiss = onDismissMenu,
            items = listOf(
                DropDownItem(
                    title = stringResource(Res.string.profile_settings),
                    icon = vectorResource(DesignSystemRes.drawable.users_icon),
                    contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                    onClick = onProfileSettingsClick
                ),
                DropDownItem(
                    title = stringResource(Res.string.logout),
                    icon = vectorResource(DesignSystemRes.drawable.log_out_icon),
                    contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                    onClick = onLogoutClick
                )
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ChatListHeaderPreview() {
    HeyAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ChatListHeader(
                localParticipant = ChatParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH",
                ),
                isUserMenuOpen = true,
                onUserAvatarClick = {},
                onDismissMenu = {},
                onProfileSettingsClick = {},
                onLogoutClick = {}
            )
        }
    }
}