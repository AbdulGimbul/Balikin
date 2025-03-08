package dev.balikin.poject.features.auth.presentation.set_password

data class SetNewPasswordUiState(
    val password: String = "",
    val confirmPassword: String = "",
    val passwordMismatch: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

