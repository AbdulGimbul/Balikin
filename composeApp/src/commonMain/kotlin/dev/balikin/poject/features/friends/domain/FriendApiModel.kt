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
