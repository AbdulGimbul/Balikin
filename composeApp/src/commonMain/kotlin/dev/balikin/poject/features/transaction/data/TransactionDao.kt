package dev.balikin.poject.features.transaction.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    suspend fun getTotalAmountByType(type: String): Double?

    @Query(
        "SELECT * FROM transactions WHERE " +
                "(:type IS NULL OR type = :type) AND " +
                "(createdAt BETWEEN :startDate AND :endDate) AND isPaid = 0 " +
                "ORDER BY CASE WHEN :sortOrder = 'asc' THEN amount END ASC, " +
                "CASE WHEN :sortOrder = 'desc' THEN amount END DESC"
    )
    fun getFilteredTransactions(
        type: String?,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        sortOrder: String
    ): Flow<List<TransactionEntity>>

    @Query(
        "SELECT COUNT(*) FROM transactions WHERE " +
                "(:type IS NULL OR type = :type) AND isPaid = 0 AND " +
                "(createdAt BETWEEN :startDate AND :endDate)"
    )
    suspend fun countFilteredTransactions(
        type: String?,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Int

    @Query("UPDATE transactions SET isPaid = 1, paidAt = :paidAt, updatedAt = :updatedAt WHERE id = :transactionId")
    suspend fun markTransactionAsPaid(
        transactionId: Long,
        paidAt: LocalDateTime,
        updatedAt: LocalDateTime
    )
}