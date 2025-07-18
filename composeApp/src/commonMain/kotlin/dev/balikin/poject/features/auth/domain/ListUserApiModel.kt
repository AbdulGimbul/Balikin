package dev.balikin.poject.features.auth.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUsersApiModel(
    val status: Int,
    val data: List<ListUserData>,
    @SerialName("meta_info")
    val metaInfo: MetaInfo,
    val message: String
)

@Serializable
data class ListUserData(
    val name: String,
    val email: String,
    @SerialName("is_online")
    val isOnline: Boolean,
    @SerialName("last_online")
    val lastOnline: String?
)

@Serializable
data class MetaInfo(
    val limit: Int,
    val offset: Int,
    val total: Int
)
