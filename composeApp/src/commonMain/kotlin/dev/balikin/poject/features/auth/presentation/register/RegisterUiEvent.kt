package dev.balikin.poject.features.auth.presentation.register

sealed class RegisterUiEvent {
    data class UsernameChanged(val username: String) : RegisterUiEvent()
    data class PasswordChanged(val password: String) : RegisterUiEvent()
    data class RepeatedPasswordChanged(val repeatedPassword: String) : RegisterUiEvent()
    data class OnTermsAccepted(val isAccepted: Boolean) : RegisterUiEvent()
    data object Submit : RegisterUiEvent()
}