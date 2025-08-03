package dev.balikin.poject.features.auth.presentation.profile

import dev.balikin.poject.features.auth.domain.UserData
import dev.balikin.poject.features.friends.domain.FollowedUser

data class ProfileUiState(
    val userData: UserData? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLogout: Boolean = false,
    val friends: List<FollowedUser> = emptyList(),
    val isLoadingFriends: Boolean = false
)
