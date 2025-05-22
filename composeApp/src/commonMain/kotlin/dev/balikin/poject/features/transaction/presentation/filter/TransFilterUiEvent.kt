package dev.balikin.poject.features.transaction.presentation.filter

import dev.balikin.poject.features.transaction.data.TransactionType
import kotlinx.datetime.LocalDateTime

sealed class TransFilterUiEvent {
    data class OnTypeChanged(val type: TransactionType?) : TransFilterUiEvent()
    data class OnSortOrderChanged(val sortOrder: String?) : TransFilterUiEvent()
    data class OnStartDateChanged(val startDate: LocalDateTime?) : TransFilterUiEvent()
    data class OnEndDateChanged(val endDate: LocalDateTime?) : TransFilterUiEvent()
    object OnPreviewResultCount : TransFilterUiEvent()
    object OnApplyFilters : TransFilterUiEvent()
}