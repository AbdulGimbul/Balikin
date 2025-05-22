package dev.balikin.poject.features.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.history.data.HistoryRepository
import dev.balikin.poject.features.history.presentation.filter.HistoryFilterUiEvent
import dev.balikin.poject.features.history.presentation.filter.HistoryFilterUiState
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

    private val _filterUiState = MutableStateFlow(HistoryFilterUiState())
    val filterUiState = _filterUiState.asStateFlow()

    private val defaultStartDate = getLastWeekDate()
    private val defaultEndDate = getCurrentDate()

    init {
        getAllTransactions()
    }

    fun onEvent(uiEvent: HistoryUiEvent) {
        when (uiEvent) {
            is HistoryUiEvent.OnRemoveDate -> removeDateFilter()
            is HistoryUiEvent.OnRemoveSort -> removeSortFilter()
            is HistoryUiEvent.OnRemoveType -> removeTypeFilter()
            is HistoryUiEvent.OnQueryChanged -> {
                searchTransactionsByName(uiEvent.query)
            }
        }
    }

    fun onEventFilter(uiEvent: HistoryFilterUiEvent) {
        when (uiEvent) {
            is HistoryFilterUiEvent.OnApplyFilters -> {
                applyUserFilters(
                    type = _filterUiState.value.selectedType,
                    sortOrder = _filterUiState.value.selectedSortOrder,
                    startDate = _filterUiState.value.startDate,
                    endDate = _filterUiState.value.endDate
                )
            }

            is HistoryFilterUiEvent.OnEndDateChanged -> {
                _filterUiState.update { currentState ->
                    currentState.copy(endDate = uiEvent.endDate)
                }
            }

            is HistoryFilterUiEvent.OnStartDateChanged -> {
                _filterUiState.update { currentState ->
                    currentState.copy(startDate = uiEvent.startDate)
                }
            }

            is HistoryFilterUiEvent.OnSortOrderChanged -> {
                _filterUiState.update { currentState ->
                    currentState.copy(selectedSortOrder = uiEvent.sortOrder)
                }
            }

            is HistoryFilterUiEvent.OnTypeChanged -> {
                _filterUiState.update { currentState ->
                    currentState.copy(selectedType = uiEvent.type)
                }
            }

            is HistoryFilterUiEvent.OnPreviewResultCount -> {
                previewFilterResultCount(
                    type = filterUiState.value.selectedType,
                    startDate = filterUiState.value.startDate,
                    endDate = filterUiState.value.endDate
                )
            }
        }
    }

    fun getAllTransactions() {
        viewModelScope.launch {
            historyRepository.getAllHistories()
                .collect { transactions ->
                    _uiState.value = _uiState.value.copy(transactions = transactions)
                }
        }
    }

    fun searchTransactionsByName(query: String) {
        viewModelScope.launch {
            historyRepository.searchTransactionsByName(query)
                .collect { transactions ->
                    _uiState.value = _uiState.value.copy(transactions = transactions, nameSearch = query)
                }
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

        viewModelScope.launch {
            historyRepository.getHistoryTransactions(
                type?.name,
                startDate,
                endDate,
                sortOrder
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
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ) {
        viewModelScope.launch {
            val count = historyRepository.countFilteredHistorys(
                type?.name,
                startDate,
                endDate
            )
            _filterUiState.value = _filterUiState.value.copy(count)
        }
    }
}