package dev.balikin.poject.features.history.data

import dev.balikin.poject.features.transaction.data.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface HistoryRepository {

    fun getHistoryTransactions(
        type: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        sortOrder: String?
    ): Flow<List<TransactionEntity>>

    suspend fun countFilteredHistorys(
        type: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): Int

    suspend fun getAllHistories(): Flow<List<TransactionEntity>>

    fun searchTransactionsByName(query: String): Flow<List<TransactionEntity>>
}
