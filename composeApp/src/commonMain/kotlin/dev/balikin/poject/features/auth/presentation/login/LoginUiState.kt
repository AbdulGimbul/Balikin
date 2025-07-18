package dev.balikin.poject.features.auth.presentation.login

sealed class LoginUiState {
    data class NotAuthenticated(
        val username: String = "",
        val password: String = "",
        val isRemember: Boolean = false,
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val showWebView: Boolean = false,
    ) : LoginUiState()

    data object Authenticated : LoginUiState()
}