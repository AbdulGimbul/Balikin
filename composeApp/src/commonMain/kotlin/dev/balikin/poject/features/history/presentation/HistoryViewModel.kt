package dev.balikin.poject.features.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.history.data.HistoryRepository
import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.features.transaction.presentation.FilterParameters
import dev.balikin.poject.utils.getCurrentDate
import dev.balikin.poject.utils.getLastWeekDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class HistoryViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    private val defaultStartDate = getLastWeekDate()
    private val defaultEndDate = getCurrentDate()

    private val _filterPreviewCount = MutableStateFlow(0)
    val filterPreviewCount = _filterPreviewCount.asStateFlow()

    init {
        applyDefaultFilters()
    }

    fun onEvent(uiEvent: HistoryUiEvent){
        when (uiEvent) {
            is HistoryUiEvent.OnRemoveDate -> removeDateFilter()
            is HistoryUiEvent.OnRemoveSort -> removeSortFilter()
            is HistoryUiEvent.OnRemoveType -> removeTypeFilter()
        }
    }

    private fun applyDefaultFilters() {
        applyFilters(
            type = null,
            sortOrder = "asc",
            startDate = defaultStartDate,
            endDate = defaultEndDate,
            isUserApplied = false
        )
    }

    fun applyUserFilters(
        type: TransactionType?,
        sortOrder: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ) {
        applyFilters(
            type = type,
            sortOrder = sortOrder,
            startDate = startDate,
            endDate = endDate,
            isUserApplied = true
        )
    }

    private fun applyFilters(
        type: TransactionType?,
        sortOrder: String?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        isUserApplied: Boolean
    ) {
        val actualSort = sortOrder ?: "asc"
        val actualStart = startDate ?: defaultStartDate
        val actualEnd   = endDate   ?: defaultEndDate

        viewModelScope.launch {
            historyRepository.getHistoryTransactions(
                type?.name,
                actualStart,
                actualEnd,
                actualSort
            ).collect { transactions ->
                _uiState.update { currentState ->
                    currentState.copy(
                        transactions = transactions,
                        appliedFilters = if (isUserApplied) {
                            FilterParameters(type, sortOrder, startDate, endDate)
                        } else {
                            null
                        }
                    )
                }
            }
        }
    }

    fun removeTypeFilter() = _uiState.value.appliedFilters?.let {
        applyUserFilters(null, it.sortOrder, it.startDate, it.endDate)
    }

    fun removeSortFilter() = _uiState.value.appliedFilters?.let {
        applyUserFilters(it.type, null, it.startDate, it.endDate)
    }

    fun removeDateFilter() = _uiState.value.appliedFilters?.let {
        applyUserFilters(it.type, it.sortOrder, null, null)
    }

    fun previewFilterResultCount(
        type: TransactionType?,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ) {
        viewModelScope.launch {
            val count = historyRepository.countFilteredHistorys(
                type?.name,
                startDate,
                endDate
            )
            _filterPreviewCount.value = count
        }
    }


}