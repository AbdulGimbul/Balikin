package dev.balikin.poject.features.transaction.data

import kotlinx.coroutines.flow.Flow

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
}