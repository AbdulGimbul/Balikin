package dev.balikin.poject.network

import kotlinx.serialization.Serializable

@Serializable
data class DefaultError(
    val message: String
)