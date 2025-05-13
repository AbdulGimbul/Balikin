package dev.balikin.poject.features.transaction.data

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface TransactionRepository {
    suspend fun getAllTransactions(): Flow<List<TransactionEntity>>
    suspend fun addTransaction(transactionEntity: TransactionEntity)
    suspend fun getTotalAmountByType(type: TransactionType): Double?

    fun getFilteredTransactions(
        type: String?,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        sortOrder: String
    ): Flow<List<TransactionEntity>>

    suspend fun countFilteredTransactions(
        type: String?,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Int
}