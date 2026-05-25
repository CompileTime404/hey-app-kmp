package com.dorianbanic.auth.presentation.email_verification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dorianbanic.core.desingsystem.components.brand.HeyappFailureIcon
import com.dorianbanic.core.desingsystem.components.brand.HeyappSuccessIcon
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButton
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButtonStyle
import com.dorianbanic.core.desingsystem.components.layouts.HeyappAdaptiveResultLayout
import com.dorianbanic.core.desingsystem.components.layouts.HeyappSimpleResultLayout
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.desingsystem.theme.extended
import heyapp.feature.auth.presentation.generated.resources.Res
import heyapp.feature.auth.presentation.generated.resources.close
import heyapp.feature.auth.presentation.generated.resources.email_verified_failed
import heyapp.feature.auth.presentation.generated.resources.email_verified_failed_desc
import heyapp.feature.auth.presentation.generated.resources.email_verified_successfully
import heyapp.feature.auth.presentation.generated.resources.email_verified_successfully_desc
import heyapp.feature.auth.presentation.generated.resources.login
import heyapp.feature.auth.presentation.generated.resources.verifying_account
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EmailVerificationRoot(
    viewModel: EmailVerificationViewModel = koinViewModel(),
    onLoginClick:() -> Unit,
    onCloseClick:() -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EmailVerificationScreen(
        state = state,
        onAction = { action ->
            when (action) {
                EmailVerificationAction.OnCloseClick -> onCloseClick()
                EmailVerificationAction.OnLoginClick -> onLoginClick()
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun EmailVerificationScreen(
    state: EmailVerificationState,
    onAction: (EmailVerificationAction) -> Unit,
) {
    HeyappAdaptiveResultLayout {
        when {
            state.isVerifying -> {
                VerifyingContent(
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            state.isVerified -> {
                HeyappSimpleResultLayout(
                    title = stringResource(Res.string.email_verified_successfully),
                    description = stringResource(Res.string.email_verified_successfully_desc),
                    icon = {
                        HeyappSuccessIcon()
                    },
                    primaryButton = {
                        HeyappButton(
                            text = stringResource(Res.string.login),
                            onClick = {
                                onAction(EmailVerificationAction.OnLoginClick)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }
            else -> {
                HeyappSimpleResultLayout(
                    title = stringResource(Res.string.email_verified_failed),
                    description = stringResource(Res.string.email_verified_failed_desc),
                    icon = {
                        Spacer(Modifier.height(32.dp))
                        HeyappFailureIcon(
                            modifier = Modifier
                                .size(80.dp)
                        )
                        Spacer(Modifier.height(32.dp))
                    },
                    primaryButton = {
                        HeyappButton(
                            text = stringResource(Res.string.close),
                            onClick = {
                                onAction(EmailVerificationAction.OnCloseClick)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            style = HeyappButtonStyle.SECONDARY
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun VerifyingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .heightIn(min = 200.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(Res.string.verifying_account),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.extended.textSecondary
        )
    }
}

@Preview()
@Composable
private fun EmailVerificationErrorPreview() {
    HeyAppTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(),
            onAction = {}
        )
    }
}

@Preview()
@Composable
private fun EmailVerificationVerifyingPreview() {
    HeyAppTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerifying = true
            ),
            onAction = {}
        )
    }
}

@Preview()
@Composable
private fun EmailVerificationSuccessPreview() {
    HeyAppTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerified = true
            ),
            onAction = {}
        )
    }
}