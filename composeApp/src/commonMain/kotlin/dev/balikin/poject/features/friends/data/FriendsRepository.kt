package dev.balikin.poject.features.friends.data

import dev.balikin.poject.features.friends.domain.AddFriendRequest
import dev.balikin.poject.features.friends.domain.AddFriendResponse
import dev.balikin.poject.features.friends.domain.FriendApiModel
import dev.balikin.poject.network.NetworkException
import dev.balikin.poject.network.NetworkResult

interface FriendsRepository {
    suspend fun searchFriends(
        keyword: String,
        limit: String,
        offset: String
    ): NetworkResult<FriendApiModel, NetworkException>
    
    suspend fun addFriend(
        friendEmail: String
    ): NetworkResult<AddFriendResponse, NetworkException>
}
