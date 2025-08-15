package dev.balikin.poject.features.transaction.domain

import kotlinx.serialization.Serializable

@Serializable
data class CreateOnlineTransactionApiModel(
    val status: Int,
    val data: String?, // Can be null based on your response
    val message: String
)

@Serializable
data class CreateOnlineTransactionRequest(
    val email: String,
    val nominal: String,
    val kategori: String, // "UTANG" or "PIUTANG"
    val desc: String
)