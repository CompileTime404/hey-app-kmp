package com.dorianbanic.core.desingsystem.components.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HeyappStackedAvatars(
    avatars: List<ChatParticipantUi>,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.SMALL,
    maxVisible: Int = 2,
    overlapPercentage: Float = 0.4f
) {
    val overlapOffset = -(size.dp * overlapPercentage)
    val visibleAvatars = avatars.take(maxVisible)
    val remainingCount = (avatars.size - maxVisible).coerceAtLeast(0)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(overlapOffset),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleAvatars.forEach { avatarUi ->
            HeyappAvatarPhoto(
                displayText = avatarUi.initials,
                size = size,
                imageUrl = avatarUi.imageUrl
            )
        }
        if (remainingCount > 0){
            HeyappAvatarPhoto(
                displayText = "+$remainingCount",
                size = size
            )
        }
    }
}

@Composable
@Preview
private fun HeyappStackedAvatarsPreview() {
    HeyAppTheme {
        HeyappStackedAvatars(
            avatars = listOf(
                ChatParticipantUi(
                    id = "1",
                    username = "Dorian",
                    initials = "DB",
                ),
                ChatParticipantUi(
                    id = "2",
                    username = "Max",
                    initials = "MP",
                ),
                ChatParticipantUi(
                    id = "3",
                    username = "Zvonko",
                    initials = "ZP",
                )
            )
        )
    }
}