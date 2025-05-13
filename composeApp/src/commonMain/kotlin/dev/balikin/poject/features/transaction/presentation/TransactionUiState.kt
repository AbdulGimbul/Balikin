package dev.balikin.poject.features.transaction.presentation

import dev.balikin.poject.features.transaction.data.TransactionEntity
import dev.balikin.poject.features.transaction.data.TransactionType
import kotlinx.datetime.LocalDateTime

data class TransactionUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val appliedFilters: FilterParameters? = null
)

data class FilterParameters(
    val type: TransactionType?,
    val sortOrder: String?,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?
)