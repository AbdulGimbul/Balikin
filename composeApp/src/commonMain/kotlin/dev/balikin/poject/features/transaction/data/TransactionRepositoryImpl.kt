package dev.balikin.poject.features.transaction.data

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override suspend fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }

    override suspend fun addTransaction(transactionEntity: TransactionEntity) {
        transactionDao.addTransaction(transactionEntity)
    }

    override suspend fun getTotalAmountByType(type: TransactionType): Double? {
        return transactionDao.getTotalAmountByType(type.name) ?: 0.0
    }

    override fun getFilteredTransactions(
        type: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        sortOrder: String?
    ): Flow<List<TransactionEntity>> {
        return transactionDao.getFilteredTransactions(
            type,
            startDate,
            endDate,
            sortOrder
        )
    }

    override suspend fun countFilteredTransactions(
        type: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): Int {
        return transactionDao.countFilteredTransactions(type, startDate, endDate)
    }

    override suspend fun markTransactionAsPaid(transactionId: Long) {
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        transactionDao.markTransactionAsPaid(
            transactionId = transactionId,
            paidAt = currentTime,
            updatedAt = currentTime
        )
    }

    override fun searchTransactionsByName(query: String): Flow<List<TransactionEntity>> {
        return transactionDao.searchTransactionsByName(query)
    }
}