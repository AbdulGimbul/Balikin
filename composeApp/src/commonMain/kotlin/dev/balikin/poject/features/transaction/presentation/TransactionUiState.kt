package dev.balikin.poject.features.transaction.presentation

import dev.balikin.poject.features.transaction.data.TransactionEntity
import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.features.transaction.domain.UnifiedTransaction
import kotlinx.datetime.LocalDateTime

data class TransactionUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val unifiedTransactions: List<UnifiedTransaction> = emptyList(),
    val isLoadingOnline: Boolean = false,
    val onlineError: String? = null,
    val appliedFilters: FilterParameters? = null,
    val showDialog: Boolean = false,
    val nameSearch: String = "",
    val selectedTransactionId: Long? = null
)

data class FilterParameters(
    val type: TransactionType?,
    val sortOrder: String?,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?
)