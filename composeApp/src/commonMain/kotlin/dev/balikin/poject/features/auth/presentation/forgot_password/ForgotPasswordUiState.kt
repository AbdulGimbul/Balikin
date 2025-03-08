package dev.balikin.poject.features.auth.presentation.forgot_password

data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

