package dev.balikin.poject.features.history.presentation

import dev.balikin.poject.features.transaction.data.TransactionEntity
import dev.balikin.poject.features.transaction.presentation.FilterParameters

data class HistoryUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val appliedFilters: FilterParameters? = null
)