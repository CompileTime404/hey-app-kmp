package com.dorianbanic.core.desingsystem.components.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButton
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButtonStyle
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.desingsystem.theme.extended
import heyapp.core.desingsystem.generated.resources.Res
import heyapp.core.desingsystem.generated.resources.success_checkmark
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HeyappSimpleResultLayout(
    title: String,
    description: String,
    icon: @Composable ColumnScope.() -> Unit,
    primaryButton: @Composable () -> Unit,
    secondaryButton: @Composable (() -> Unit)? = null,
    secondaryError: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -(25).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            primaryButton()
            if (secondaryButton != null){
                Spacer(Modifier.height(8.dp))
                secondaryButton()
                if (secondaryError != null) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = secondaryError,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun HeyappSimpleSuccessLayoutPreview() {
    HeyAppTheme {
        HeyappSimpleResultLayout(
            title = "Registration successful!",
            description = "You can now log in to your account.",
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.success_checkmark),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.extended.success,
                )
            },
            primaryButton = {
                HeyappButton(
                    text = "Log in",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryButton = {
                HeyappButton(
                    text = "Dismiss",
                    onClick = {},
                    style = HeyappButtonStyle.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        )
    }
}