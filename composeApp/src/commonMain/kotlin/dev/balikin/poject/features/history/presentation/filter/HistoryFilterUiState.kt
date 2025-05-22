package dev.balikin.poject.features.history.presentation.filter

import dev.balikin.poject.features.transaction.data.TransactionType
import kotlinx.datetime.LocalDateTime

data class HistoryFilterUiState(
    val filterCount: Int = 0,
    val selectedType: TransactionType? = null,
    val selectedSortOrder: String? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null
)