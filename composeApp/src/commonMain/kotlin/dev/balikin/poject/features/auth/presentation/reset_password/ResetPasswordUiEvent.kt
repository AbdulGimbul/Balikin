package dev.balikin.poject.features.auth.presentation.reset_password

sealed class ResetPasswordUiEvent {
    data class OtpValueChanged(val index: Int, val value: String) : ResetPasswordUiEvent()
    data object VerifyOtp : ResetPasswordUiEvent()
    data object ResendCode : ResetPasswordUiEvent()
}

