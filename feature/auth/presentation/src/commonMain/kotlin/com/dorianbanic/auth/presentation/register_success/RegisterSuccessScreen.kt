package com.dorianbanic.auth.presentation.register_success

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dorianbanic.core.desingsystem.components.brand.HeyappSuccessIcon
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButton
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButtonStyle
import com.dorianbanic.core.desingsystem.components.layouts.HeyappAdaptiveResultLayout
import com.dorianbanic.core.desingsystem.components.layouts.HeyappSimpleResultLayout
import com.dorianbanic.core.desingsystem.components.layouts.HeyappSnackbarScaffold
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.presentation.util.ObserveAsEvents
import heyapp.feature.auth.presentation.generated.resources.Res
import heyapp.feature.auth.presentation.generated.resources.account_successfully_created
import heyapp.feature.auth.presentation.generated.resources.login
import heyapp.feature.auth.presentation.generated.resources.resend_verification_email
import heyapp.feature.auth.presentation.generated.resources.resent_verification_email
import heyapp.feature.auth.presentation.generated.resources.verification_email_sent_to_x
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterSuccessRoot(
    viewModel: RegisterSuccessViewModel = koinViewModel(),
    onLoginClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(
        viewModel.events
    ) { event ->
        when (event) {
            RegisterSuccessEvent.ResendVerificationEmailSuccess -> {
                snackbarHostState.showSnackbar(
                    message = getString(Res.string.resent_verification_email)
                )
            }
        }
    }

    RegisterSuccessScreen(
        state = state,
        onAction = { action ->
            when (action) {
                RegisterSuccessAction.OnLoginClick -> onLoginClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun RegisterSuccessScreen(
    state: RegisterSuccessState,
    onAction: (RegisterSuccessAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    HeyappSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        HeyappAdaptiveResultLayout {
            HeyappSimpleResultLayout(
                title = stringResource(Res.string.account_successfully_created),
                description = stringResource(Res.string.verification_email_sent_to_x, state.registeredEmail),
                icon = {
                    HeyappSuccessIcon()
                },
                primaryButton = {
                    HeyappButton(
                        text = stringResource(Res.string.login),
                        onClick = {
                            onAction(RegisterSuccessAction.OnLoginClick)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                },
                secondaryButton = {
                    HeyappButton(
                        text = stringResource(Res.string.resend_verification_email),
                        onClick = {
                            onAction(RegisterSuccessAction.OnResendVerificationEmailClick)
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = !state.isResendingVerificationEmail,
                        isLoading = state.isResendingVerificationEmail,
                        style = HeyappButtonStyle.SECONDARY
                    )
                },
                secondaryError = state.resendVerificationError?.asString()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    HeyAppTheme {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "william.paterson@my-own-personal-domain.com"
            ),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}