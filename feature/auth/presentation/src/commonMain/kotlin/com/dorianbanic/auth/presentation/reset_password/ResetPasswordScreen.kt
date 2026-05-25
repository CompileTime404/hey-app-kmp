package com.dorianbanic.auth.presentation.reset_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButton
import com.dorianbanic.core.desingsystem.components.layouts.HeyappAdaptiveFormLayout
import com.dorianbanic.core.desingsystem.components.textfields.HeyappPasswordTextField
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.desingsystem.theme.extended
import heyapp.feature.auth.presentation.generated.resources.Res
import heyapp.feature.auth.presentation.generated.resources.new_password
import heyapp.feature.auth.presentation.generated.resources.password
import heyapp.feature.auth.presentation.generated.resources.password_hint
import heyapp.feature.auth.presentation.generated.resources.reset_password_successfully
import heyapp.feature.auth.presentation.generated.resources.set_new_password
import heyapp.feature.auth.presentation.generated.resources.submit
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordRoot(
    viewModel: ResetPasswordViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ResetPasswordScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ResetPasswordScreen(
    state: ResetPasswordState,
    onAction: (ResetPasswordAction) -> Unit,
) {
    HeyappAdaptiveFormLayout(
        headerText = stringResource(Res.string.set_new_password),
        errorText = state.errorText?.asString(),
    ) {
        HeyappPasswordTextField(
            state = state.passwordTextFieldState,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = stringResource(Res.string.password),
            title = stringResource(Res.string.new_password),
            supportingText = stringResource(Res.string.password_hint),
            isPasswordVisible = state.isPasswordVisible,
            onToggleVisibilityClick = {
                onAction(ResetPasswordAction.onTogglePasswordVisibilityClick)
            },
        )
        Spacer(Modifier.height(16.dp))
        HeyappButton(
            text = stringResource(Res.string.submit),
            onClick = {
                onAction(ResetPasswordAction.onSubmitClick)
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = !state.isLoading && state.canSubmit,
            isLoading = state.isLoading
        )
        if (state.isResetSuccessful) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.reset_password_successfully),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.extended.success,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    HeyAppTheme {
        ResetPasswordScreen(
            state = ResetPasswordState(),
            onAction = {}
        )
    }
}