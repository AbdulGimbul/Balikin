package dev.balikin.poject.features.friends.data

import dev.balikin.poject.features.friends.domain.AddFriendRequest
import dev.balikin.poject.features.friends.domain.AddFriendResponse
import dev.balikin.poject.features.friends.domain.FollowingApiModel
import dev.balikin.poject.features.friends.domain.FriendApiModel
import dev.balikin.poject.network.NetworkException
import dev.balikin.poject.network.NetworkResult
import dev.balikin.poject.network.RequestHandler

class FriendsRepositoryImpl(
    private val requestHandler: RequestHandler
) : FriendsRepository {
    override suspend fun searchFriends(
        keyword: String,
        limit: String,
        offset: String
    ): NetworkResult<FriendApiModel, NetworkException> {
        return requestHandler.get<FriendApiModel>(
            urlPathSegments = listOf("api", "v1", "list-users"),
            queryParams = mapOf(
                "keyword" to keyword,
                "limit" to limit,
                "offset" to offset
            )
        )
    }
    
    override suspend fun addFriend(
        friendEmail: String
    ): NetworkResult<AddFriendResponse, NetworkException> {
        return requestHandler.post<AddFriendRequest, AddFriendResponse>(
            urlPathSegments = listOf("api", "v1", "following"),
            body = AddFriendRequest(friendEmail = friendEmail)
        )
    }
    
    override suspend fun getFollowing(): NetworkResult<FollowingApiModel, NetworkException> {
        return requestHandler.get<FollowingApiModel>(
            urlPathSegments = listOf("api", "v1", "following")
        )
    }
}
