package com.dorianbanic.auth.presentation.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButton
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButtonStyle
import com.dorianbanic.core.desingsystem.components.layouts.HeyappAdaptiveFormLayout
import com.dorianbanic.core.desingsystem.components.layouts.HeyappSnackbarScaffold
import com.dorianbanic.core.desingsystem.components.textfields.HeyappPasswordTextField
import com.dorianbanic.core.desingsystem.components.textfields.HeyappTextField
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.presentation.util.ObserveAsEvents
import heyapp.feature.auth.presentation.generated.resources.Res
import heyapp.feature.auth.presentation.generated.resources.email
import heyapp.feature.auth.presentation.generated.resources.email_placeholder
import heyapp.feature.auth.presentation.generated.resources.login
import heyapp.feature.auth.presentation.generated.resources.password
import heyapp.feature.auth.presentation.generated.resources.password_hint
import heyapp.feature.auth.presentation.generated.resources.register
import heyapp.feature.auth.presentation.generated.resources.username
import heyapp.feature.auth.presentation.generated.resources.username_hint
import heyapp.feature.auth.presentation.generated.resources.username_placeholder
import heyapp.feature.auth.presentation.generated.resources.welcome_to_heyapp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterRoot(
    viewModel: RegisterViewModel = koinViewModel(),
    onRegisterSuccess: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RegisterEvent.Success -> {
                onRegisterSuccess(event.email)
            }
        }
    }

    RegisterScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is RegisterAction.OnLoginClick -> onLoginClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    HeyappSnackbarScaffold(
        snackbarHostState = snackbarHostState,
    ) {
        HeyappAdaptiveFormLayout(
            headerText = stringResource(Res.string.welcome_to_heyapp),
            errorText = state.registrationError?.asString(),
        ) {
            HeyappTextField(
                state = state.usernameTextState,
                placeholder = stringResource(Res.string.username),
                title = stringResource(Res.string.username),
                isError = state.usernameError != null,
                onFocusChanged = {
                    onAction(RegisterAction.OnInputTextFocusGain)
                }
            )
            Spacer(Modifier.height(16.dp))
            HeyappTextField(
                state = state.emailTextState,
                placeholder = stringResource(Res.string.email),
                title = stringResource(Res.string.email),
                isError = state.emailError != null,
                onFocusChanged = {
                    onAction(RegisterAction.OnInputTextFocusGain)
                },
                keyboardType = KeyboardType.Email
            )
            Spacer(Modifier.height(16.dp))
            HeyappPasswordTextField(
                state = state.passwordTextState,
                placeholder = stringResource(Res.string.password),
                title = stringResource(Res.string.password),
                isError = state.passwordError != null,
                onFocusChanged = {
                    onAction(RegisterAction.OnInputTextFocusGain)
                },
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                }
            )
            Spacer(Modifier.height(16.dp))
            HeyappButton(
                text = stringResource(Res.string.register),
                onClick = {
                    onAction(RegisterAction.OnRegisterClick)
                },
                enabled = state.canRegister,
                isLoading = state.isRegistering,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            HeyappButton(
                text = stringResource(Res.string.login),
                onClick = {
                    onAction(RegisterAction.OnLoginClick)
                },
                style = HeyappButtonStyle.SECONDARY,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    HeyAppTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
            snackbarHostState = remember {
                SnackbarHostState()
            }
        )
    }
}