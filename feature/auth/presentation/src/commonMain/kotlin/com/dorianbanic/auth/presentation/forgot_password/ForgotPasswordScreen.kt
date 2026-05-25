package com.dorianbanic.auth.presentation.forgot_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dorianbanic.core.desingsystem.components.buttons.HeyappButton
import com.dorianbanic.core.desingsystem.components.layouts.HeyappAdaptiveFormLayout
import com.dorianbanic.core.desingsystem.components.textfields.HeyappTextField
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.desingsystem.theme.extended
import heyapp.feature.auth.presentation.generated.resources.Res
import heyapp.feature.auth.presentation.generated.resources.email
import heyapp.feature.auth.presentation.generated.resources.email_placeholder
import heyapp.feature.auth.presentation.generated.resources.forgot_password
import heyapp.feature.auth.presentation.generated.resources.forgot_password_email_sent_successfully
import heyapp.feature.auth.presentation.generated.resources.submit
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordRoot(
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ForgotPasswordScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit,
) {
    HeyappAdaptiveFormLayout(
        headerText = stringResource(Res.string.forgot_password),
        errorText = state.errorText?.asString(),
    ) {
        HeyappTextField(
            state = state.emailTextFieldState,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = stringResource(Res.string.email_placeholder),
            title = stringResource(Res.string.email),
            isError = state.errorText != null,
            singleLine = true,
            keyboardType = KeyboardType.Email,
            supportingText = state.errorText?.asString()
        )
        Spacer(Modifier.height(16.dp))
        HeyappButton(
            text = stringResource(Res.string.submit),
            onClick = {
                onAction(ForgotPasswordAction.OnSubmitClick)
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = !state.isLoading && state.canSubmit,
            isLoading = state.isLoading
        )
        Spacer(Modifier.height(16.dp))
        if (state.isEmailSentSuccessfully) {
            Text(
                text = stringResource(Res.string.forgot_password_email_sent_successfully),
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
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            onAction = {}
        )
    }
}