package dev.balikin.poject.features.auth.presentation.login

sealed class LoginUiEvent {
    data class UsernameChanged(val username: String) : LoginUiEvent()
    data class PasswordChanged(val password: String) : LoginUiEvent()
    data class OnRemember(val isRemember: Boolean) : LoginUiEvent()
    data object Login : LoginUiEvent()
}