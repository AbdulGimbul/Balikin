package dev.balikin.poject.features.history.presentation.filter

import dev.balikin.poject.features.transaction.data.TransactionType
import kotlinx.datetime.LocalDateTime

sealed class HistoryFilterUiEvent {
    data class OnTypeChanged(val type: TransactionType?) : HistoryFilterUiEvent()
    data class OnSortOrderChanged(val sortOrder: String) : HistoryFilterUiEvent()
    data class OnStartDateChanged(val startDate: LocalDateTime) : HistoryFilterUiEvent()
    data class OnEndDateChanged(val endDate: LocalDateTime) : HistoryFilterUiEvent()
    object OnPreviewResultCount: HistoryFilterUiEvent()
    object OnApplyFilters: HistoryFilterUiEvent()
}