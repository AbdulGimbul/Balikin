package dev.balikin.poject.features.auth.presentation.profile

import dev.balikin.poject.features.auth.domain.UserData

data class ProfileUiState(
    val userData: UserData? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLogout: Boolean = false
)
