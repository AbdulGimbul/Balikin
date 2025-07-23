package dev.balikin.poject.features.friends.presentation

import dev.balikin.poject.features.transaction.presentation.TransactionUiEvent

sealed class FriendsUiEvent {
    data class OnQueryChanged(val query: String) : FriendsUiEvent()
}