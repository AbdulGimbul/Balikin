package dev.balikin.poject.features.friends.data

import dev.balikin.poject.features.friends.domain.FriendApiModel
import dev.balikin.poject.network.NetworkException
import dev.balikin.poject.network.NetworkResult

interface FriendsRepository {
    suspend fun searchFriends(
        keyword: String,
        limit: String,
        offset: String
    ): NetworkResult<FriendApiModel, NetworkException>
}
