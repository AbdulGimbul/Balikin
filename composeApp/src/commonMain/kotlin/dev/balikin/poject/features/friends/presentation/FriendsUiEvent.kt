package dev.balikin.poject.features.friends.presentation

sealed class FriendsUiEvent {
    data class OnQueryChanged(val query: String) : FriendsUiEvent()
}
