package dev.balikin.poject.features.transaction.data

import dev.balikin.poject.features.transaction.domain.CreateOnlineTransactionApiModel
import dev.balikin.poject.features.transaction.domain.CreateOnlineTransactionRequest
import dev.balikin.poject.features.transaction.domain.OnlineTransactionListApiModel
import dev.balikin.poject.network.NetworkException
import dev.balikin.poject.network.NetworkResult
import dev.balikin.poject.network.RequestHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class TransactionRepositoryImpl(
    private val requestHandler: RequestHandler,
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override suspend fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }

    override suspend fun addTransaction(transactionEntity: TransactionEntity): Long {
        return transactionDao.addTransaction(transactionEntity)
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

    @OptIn(ExperimentalTime::class)
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

    override suspend fun createOnlineTransaction(
        request: CreateOnlineTransactionRequest
    ): NetworkResult<CreateOnlineTransactionApiModel, NetworkException> {
        return requestHandler.post<CreateOnlineTransactionRequest, CreateOnlineTransactionApiModel>(
            urlPathSegments = listOf("api", "v1", "receivable"),
            body = request
        )
    }

    override suspend fun getOnlineTransactions(
        keyword: String,
        limit: String,
        offset: String
    ): NetworkResult<OnlineTransactionListApiModel, NetworkException> {
        return requestHandler.get<OnlineTransactionListApiModel>(
            urlPathSegments = listOf("api", "v1", "receivable"),
            queryParams = mapOf(
                "keyword" to keyword,
                "limit" to limit,
                "offset" to offset
            )
        )
    }
}