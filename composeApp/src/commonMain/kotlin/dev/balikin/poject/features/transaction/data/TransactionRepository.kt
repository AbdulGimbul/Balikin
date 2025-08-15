package dev.balikin.poject.features.transaction.data

import dev.balikin.poject.features.transaction.domain.CreateOnlineTransactionApiModel
import dev.balikin.poject.features.transaction.domain.CreateOnlineTransactionRequest
import dev.balikin.poject.features.transaction.domain.OnlineTransactionListApiModel
import dev.balikin.poject.network.NetworkException
import dev.balikin.poject.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface TransactionRepository {
    suspend fun getAllTransactions(): Flow<List<TransactionEntity>>
    suspend fun addTransaction(transactionEntity: TransactionEntity): Long
    suspend fun getTotalAmountByType(type: TransactionType): Double?

    fun getFilteredTransactions(
        type: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        sortOrder: String?
    ): Flow<List<TransactionEntity>>

    suspend fun countFilteredTransactions(
        type: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): Int

    suspend fun markTransactionAsPaid(transactionId: Long)

    fun searchTransactionsByName(query: String): Flow<List<TransactionEntity>>

    suspend fun createOnlineTransaction(
        request: CreateOnlineTransactionRequest
    ): NetworkResult<CreateOnlineTransactionApiModel, NetworkException>

    suspend fun getOnlineTransactions(
        keyword: String = "",
        limit: String = "10",
        offset: String = "0"
    ): NetworkResult<OnlineTransactionListApiModel, NetworkException>
}