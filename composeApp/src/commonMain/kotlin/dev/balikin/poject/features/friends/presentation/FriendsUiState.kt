package dev.balikin.poject.features.friends.presentation

import dev.balikin.poject.features.friends.domain.FriendApiModel
import dev.balikin.poject.features.friends.domain.FriendsApiData

data class FriendsUiState(
    val nameSearch: String = "",
    val isLoading: Boolean = false,
    val friends: List<FriendsApiData> = emptyList(),
    val errorMessage: String? = null,
)

data class SearchFilters(
    val onlineOnly: Boolean = false,
    val searchInEmail: Boolean = true,
    val searchInName: Boolean = true
)