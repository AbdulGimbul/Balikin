package dev.balikin.poject.features.transaction.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OnlineTransactionListApiModel(
    val status: Int,
    val data: List<OnlineTransaction>,
    @SerialName("meta_info")
    val metaInfo: OnlineTransactionMetaInfo,
    val message: String? = null
)

@Serializable
data class OnlineTransaction(
    val nominal: String,
    val date: String,
    val kategori: String, // "UTANG" or "PIUTANG"
    val desc: String,
    val receivable: OnlineTransactionUser,
    val payable: OnlineTransactionUser
)

@Serializable
data class OnlineTransactionUser(
    val id: Int,
    val name: String,
    val email: String,
    @SerialName("is_online")
    val isOnline: Boolean,
    @SerialName("last_online")
    val lastOnline: String
)

@Serializable
data class OnlineTransactionMetaInfo(
    val limit: Int,
    val offset: Int,
    val total: Int
)
