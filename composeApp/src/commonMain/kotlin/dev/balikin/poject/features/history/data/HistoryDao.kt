package dev.balikin.poject.features.history.data

import androidx.room.Dao
import androidx.room.Query
import dev.balikin.poject.features.transaction.data.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface HistoryDao {
    @Query("SELECT * FROM transactions WHERE isPaid = 1 ORDER BY paidAt DESC")
    fun getAllHistories(): Flow<List<TransactionEntity>>

    @Query(
        """
    SELECT COUNT(*) FROM transactions
    WHERE (:type IS NULL OR type = :type)
      AND isPaid = 1
      AND (:startDate IS NULL OR paidAt >= :startDate)
      AND (:endDate IS NULL OR paidAt <= :endDate)
    """
    )
    suspend fun countHistoryTransactions(
        type: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ): Int

    @Query(
        "SELECT * FROM transactions WHERE " +
                "(:type IS NULL OR type = :type) AND isPaid = 1 AND " +
                "(:startDate IS NULL OR paidAt >= :startDate) AND " +
                "(:endDate IS NULL OR paidAt <= :endDate) " +
                "ORDER BY " +
                "CASE WHEN :sortOrder = 'asc' THEN amount END ASC, " +
                "CASE WHEN :sortOrder = 'desc' THEN amount END DESC, " +
                "paidAt DESC"
    )
    fun getHistoryTransactions(
        type: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        sortOrder: String?
    ): Flow<List<TransactionEntity>>

    @Query(
        "SELECT * FROM transactions WHERE " +
                "name LIKE '%' || :query || '%' AND isPaid = 1 " +
                "ORDER BY createdAt DESC"
    )
    fun searchTransactionsByName(query: String): Flow<List<TransactionEntity>>
}