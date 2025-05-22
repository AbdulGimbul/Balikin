package dev.balikin.poject.features.history.data

import dev.balikin.poject.features.transaction.data.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun getHistoryTransactions(
        type: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        sortOrder: String?
    ): Flow<List<TransactionEntity>> {
        return historyDao.getHistoryTransactions(type, startDate, endDate, sortOrder)
    }

    override suspend fun countFilteredHistorys(
        type: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): Int {
        return historyDao.countHistoryTransactions(type, startDate, endDate)
    }

    override suspend fun getAllHistories(): Flow<List<TransactionEntity>> {
        return historyDao.getAllHistories()
    }

    override fun searchTransactionsByName(query: String): Flow<List<TransactionEntity>> {
        return historyDao.searchTransactionsByName(query)
    }
}