package dev.balikin.poject.features.history.presentation.filter

import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.utils.getCurrentDate
import dev.balikin.poject.utils.getLastWeekDate
import kotlinx.datetime.LocalDateTime

data class HistoryFilterUiState(
    val filterCount: Int = 0,
    val selectedType: TransactionType? = null,
    val selectedSortOrder: String = "asc",
    val startDate: LocalDateTime = getLastWeekDate(),
    val endDate: LocalDateTime = getCurrentDate()
)