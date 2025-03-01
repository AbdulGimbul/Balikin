package dev.balikin.poject.features.auth.presentation.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isRemember: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)