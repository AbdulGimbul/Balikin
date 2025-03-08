package dev.balikin.poject.features.auth.presentation.set_password

sealed class SetNewPasswordUiEvent {
    data class PasswordChanged(val password: String) : SetNewPasswordUiEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SetNewPasswordUiEvent()
    data object ResetPassword : SetNewPasswordUiEvent()
}

