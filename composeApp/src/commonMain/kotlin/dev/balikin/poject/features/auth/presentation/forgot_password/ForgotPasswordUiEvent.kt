package dev.balikin.poject.features.auth.presentation.forgot_password

sealed class ForgotPasswordUiEvent {
    data class EmailChanged(val email: String) : ForgotPasswordUiEvent()
    data object ResetPassword : ForgotPasswordUiEvent()
    data object BackToLogin : ForgotPasswordUiEvent()
}

