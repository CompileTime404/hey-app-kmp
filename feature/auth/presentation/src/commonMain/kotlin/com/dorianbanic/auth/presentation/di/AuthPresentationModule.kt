package com.dorianbanic.auth.presentation.di

import com.dorianbanic.auth.presentation.email_verification.EmailVerificationViewModel
import com.dorianbanic.auth.presentation.forgot_password.ForgotPasswordViewModel
import com.dorianbanic.auth.presentation.login.LoginViewModel
import com.dorianbanic.auth.presentation.register.RegisterViewModel
import com.dorianbanic.auth.presentation.register_success.RegisterSuccessViewModel
import com.dorianbanic.auth.presentation.reset_password.ResetPasswordViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::ResetPasswordViewModel)
}