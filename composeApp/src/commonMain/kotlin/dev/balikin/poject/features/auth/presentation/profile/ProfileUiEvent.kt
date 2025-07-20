package dev.balikin.poject.features.auth.presentation.profile

sealed class ProfileUiEvent {
    data object Logout : ProfileUiEvent()
}