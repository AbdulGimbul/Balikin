package dev.balikin.poject.features.auth.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginApiModel(
    val status: Int,
    val data: LoginDataApiModel,
    val token: String,
    val message: String
)

@Serializable
data class LoginDataApiModel(
    val id: Int,
    val name: String,
    val email: String,
    val password: String?,
    @SerialName("is_online")
    val isOnline: Boolean,
    @SerialName("last_online")
    val lastOnline: String?,
    @SerialName("created_by")
    val createdBy: String?,
    @SerialName("updated_by")
    val updatedBy: String?,
    @SerialName("role_id")
    val roleId: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String?
)