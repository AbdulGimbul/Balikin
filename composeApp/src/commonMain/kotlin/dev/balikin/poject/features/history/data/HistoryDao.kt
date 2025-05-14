package dev.balikin.poject.features.history.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.balikin.poject.features.transaction.data.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface HistoryDao {
    @Query(
        "SELECT COUNT(*) FROM transactions WHERE " +
                "(:type IS NULL OR type = :type) AND isPaid = 1 AND " +
                "(paidAt BETWEEN :startDate AND :endDate)"
    )
    suspend fun countHistoryTransactions(
        type: String?,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Int

    @Query(
        "SELECT * FROM transactions WHERE " +
                "(:type IS NULL OR type = :type) AND " +
                "(paidAt BETWEEN :startDate AND :endDate) AND isPaid = 1 " +
                "ORDER BY CASE WHEN :sortOrder = 'asc' THEN amount END ASC, " +
                "CASE WHEN :sortOrder = 'desc' THEN amount END DESC"
    )
    fun getHistoryTransactions(
        type: String?,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        sortOrder: String
    ): Flow<List<TransactionEntity>>
}