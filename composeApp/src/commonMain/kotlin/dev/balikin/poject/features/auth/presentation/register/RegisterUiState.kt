package dev.balikin.poject.features.auth.presentation.register


data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)