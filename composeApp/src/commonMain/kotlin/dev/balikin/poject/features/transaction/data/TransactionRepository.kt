package dev.balikin.poject.features.transaction.data

import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun getAllTransactions(): Flow<List<TransactionEntity>>
    suspend fun addTransaction(transactionEntity: TransactionEntity)
    suspend fun getTotalAmountByType(type: TransactionType): Double?
}