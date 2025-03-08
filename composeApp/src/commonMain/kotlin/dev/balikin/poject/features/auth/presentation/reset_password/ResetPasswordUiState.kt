package dev.balikin.poject.features.auth.presentation.reset_password

data class ResetPasswordUiState(
    val otpValues: List<String> = List(6) { "" },
    val email: String = "aguslikhsan08@gmail.com",
    val timeLeft: Int = 81,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

