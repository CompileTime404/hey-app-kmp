package com.dorianbanic.auth.presentation.login

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorianbanic.auth.domain.EmailValidator
import com.dorianbanic.core.domain.auth.AuthService
import com.dorianbanic.core.domain.auth.SessionStorage
import com.dorianbanic.core.domain.util.DataError
import com.dorianbanic.core.domain.util.onFailure
import com.dorianbanic.core.domain.util.onSuccess
import com.dorianbanic.core.presentation.util.UiText
import com.dorianbanic.core.presentation.util.toUiText
import heyapp.feature.auth.presentation.generated.resources.Res
import heyapp.feature.auth.presentation.generated.resources.error_email_not_verified
import heyapp.feature.auth.presentation.generated.resources.error_invalid_credentials
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authService: AuthService,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    private val isEmailValidFlow = snapshotFlow { state.value.emailTextFieldState.text.toString() }
        .map { EmailValidator.validate(it) }
        .distinctUntilChanged()

    private val isPasswordNotBlank = snapshotFlow { state.value.passwordTextFieldState.text.toString() }
        .map { it.isNotBlank() }
        .distinctUntilChanged()

    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeTextStates()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    private val isRegisteringFlow = state
        .map { it.isLoggingIn }
        .distinctUntilChanged()

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            LoginAction.OnTogglePasswordVisibility -> {
                _state.update { it.copy(
                    isPasswordVisible = !it.isPasswordVisible
                ) }
            }
            else -> Unit
        }
    }

    private fun observeTextStates() {
        combine(
            isEmailValidFlow,
            isPasswordNotBlank,
            isRegisteringFlow
        ) { isEmailValid, isPasswordNotBlank, isRegisteringFlow ->
            _state.update {
                it.copy(
                    canLogin = !isRegisteringFlow && isEmailValid && isPasswordNotBlank
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun login() {
        if (!state.value.canLogin) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(
                isLoggingIn = true
            ) }

            val email = state.value.emailTextFieldState.text.toString()
            val password = state.value.passwordTextFieldState.text.toString()

            authService
                .login(
                    email = email,
                    password = password
                )
                .onSuccess { authInfo ->
                    sessionStorage.set(authInfo)
                    _state.update { it.copy(
                        isLoggingIn = false,
                    ) }
                    eventChannel.send(LoginEvent.Success)
                }
                .onFailure { error ->
                    val errorMessage = when(error) {
                        DataError.Remote.UNAUTHORIZED -> UiText.Resource(Res.string.error_invalid_credentials)
                        DataError.Remote.FORBIDDEN -> UiText.Resource(Res.string.error_email_not_verified)
                        else -> error.toUiText()
                    }
                    _state.update { it.copy(
                        isLoggingIn = false,
                        error = errorMessage
                    ) }
                }
        }
    }

}