package com.dorianbanic.auth.presentation.reset_password

sealed interface ResetPasswordAction {
    data object onSubmitClick: ResetPasswordAction
    data object onTogglePasswordVisibilityClick: ResetPasswordAction
}