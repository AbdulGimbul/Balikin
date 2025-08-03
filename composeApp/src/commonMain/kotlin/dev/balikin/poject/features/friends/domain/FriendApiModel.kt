package dev.balikin.poject.features.friends.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendApiModel(
    val status: String,
    val data: List<FriendsApiData>,
    @SerialName("meta_info")
    val metaInfo: FriendsApiMetaInfo,
    val message: String
)

@Serializable
data class FriendsApiData(
    val name: String,
    val email: String,
    @SerialName("is_online")
    val isOnline: Boolean,
    @SerialName("last_online")
    val lastOnline: String?
)

@Serializable
data class FriendsApiMetaInfo(
    val limit: Int,
    val offset: Int,
    val total: Int
)

@Serializable
data class AddFriendRequest(
    @SerialName("friend_email")
    val friendEmail: String
)

@Serializable
data class AddFriendResponse(
    val status: Int,
    val data: String?,
    val message: String
)

@Serializable
data class FollowingApiModel(
    val status: Int,
    val data: List<FollowingData>,
    @SerialName("meta_info")
    val metaInfo: FollowingMetaInfo,
    val message: String
)

@Serializable
data class FollowingData(
    @SerialName("follower_id")
    val followerId: Int,
    @SerialName("followed_id")
    val followedId: Int,
    @SerialName("created_at")
    val createdAt: String,
    val followed: FollowedUser
)

@Serializable
data class FollowedUser(
    val id: Int,
    val name: String,
    val email: String,
    @SerialName("is_online")
    val isOnline: Boolean,
    @SerialName("last_online")
    val lastOnline: String?
)

@Serializable
data class FollowingMetaInfo(
    val limit: Int,
    val offset: Int,
    val total: Int
)
