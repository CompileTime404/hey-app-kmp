package com.dorianbanic.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import heyapp.feature.auth.presentation.generated.resources.create_account
import heyapp.feature.auth.presentation.generated.resources.email
import heyapp.feature.auth.presentation.generated.resources.email_placeholder
import heyapp.feature.auth.presentation.generated.resources.forgot_password
import heyapp.feature.auth.presentation.generated.resources.login
import heyapp.feature.auth.presentation.generated.resources.password
import heyapp.feature.auth.presentation.generated.resources.welcome_back
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            LoginEvent.Success -> onLoginSuccess()
        }
    }

    LoginScreen(
        state = state,
        onAction = { action ->
            when(action){
                is LoginAction.OnForgotPasswordClick -> onForgotPasswordClick()
                is LoginAction.OnSignUpClick -> onCreateAccountClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    HeyappSnackbarScaffold {
        HeyappAdaptiveFormLayout(
            headerText = stringResource(Res.string.welcome_back),
            errorText = state.error?.asString(),
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            HeyappTextField(
                state = state.emailTextFieldState,
                placeholder = stringResource(Res.string.email),
                keyboardType = KeyboardType.Email,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(Res.string.email)
            )
            Spacer(Modifier.height(16.dp))
            HeyappPasswordTextField(
                state = state.passwordTextFieldState,
                placeholder = stringResource(Res.string.password),
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(LoginAction.OnTogglePasswordVisibility)
                },
                title = stringResource(Res.string.password),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.forgot_password),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        onAction(LoginAction.OnForgotPasswordClick)
                    }
            )
            Spacer(Modifier.height(24.dp))
            HeyappButton(
                text = stringResource(Res.string.login),
                onClick = {
                    onAction(LoginAction.OnLoginClick)
                },
                enabled = state.canLogin,
                isLoading = state.isLoggingIn,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
            HeyappButton(
                text = stringResource(Res.string.create_account),
                onClick = {
                    onAction(LoginAction.OnSignUpClick)
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
private fun LoginScreenPreview() {
    HeyAppTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}