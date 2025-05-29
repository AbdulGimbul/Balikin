package dev.balikin.poject.features.transaction.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.transaction.data.TransactionRepository
import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.features.transaction.presentation.filter.TransFilterUiEvent
import dev.balikin.poject.features.transaction.presentation.filter.TransFilterUiState
import dev.balikin.poject.utils.getCurrentDate
import dev.balikin.poject.utils.getLastWeekDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class TransactionViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState = _uiState.asStateFlow()

    private val _filterUiState = MutableStateFlow(TransFilterUiState())
    val filterUiState = _filterUiState.asStateFlow()

    private val defaultStartDate = getLastWeekDate()
    private val defaultEndDate = getCurrentDate()

    init {
        getAllTransactions()
    }

    fun onEvent(uiEvent: TransactionUiEvent) {
        when (uiEvent) {
            is TransactionUiEvent.OnRemoveDate -> removeDateFilter()
            is TransactionUiEvent.OnRemoveSort -> removeSortFilter()
            is TransactionUiEvent.OnRemoveType -> removeTypeFilter()
            is TransactionUiEvent.OnPaidClicked -> {
                _uiState.value = _uiState.value.copy(
                    showDialog = true,
                    selectedTransactionId = uiEvent.id
                )
            }

            is TransactionUiEvent.OnMarkAsPaid -> {
                _uiState.value.selectedTransactionId?.let { markAsPaid(it) }
                _uiState.value = _uiState.value.copy(showDialog = false)
            }

            TransactionUiEvent.OnDismissDialog -> {
                _uiState.value = _uiState.value.copy(showDialog = false)
            }

            is TransactionUiEvent.OnQueryChanged -> {
                _uiState.value = _uiState.value.copy(nameSearch = uiEvent.query)
                searchTransactionsByName(uiEvent.query)
            }
        }
    }

    fun onEventFilter(uiEvent: TransFilterUiEvent) {
        when (uiEvent) {
            is TransFilterUiEvent.OnApplyFilters -> {
                applyUserFilters(
                    type = _filterUiState.value.selectedType,
                    sortOrder = _filterUiState.value.selectedSortOrder,
                    startDate = _filterUiState.value.startDate,
                    endDate = _filterUiState.value.endDate
                )
            }

            is TransFilterUiEvent.OnEndDateChanged -> {
                _filterUiState.update { currentState ->
                    currentState.copy(endDate = uiEvent.endDate)
                }
            }

            is TransFilterUiEvent.OnStartDateChanged -> {
                _filterUiState.update { currentState ->
                    currentState.copy(startDate = uiEvent.startDate)
                }
            }

            is TransFilterUiEvent.OnSortOrderChanged -> {
                _filterUiState.update { currentState ->
                    currentState.copy(selectedSortOrder = uiEvent.sortOrder)
                }
            }

            is TransFilterUiEvent.OnTypeChanged -> {
                _filterUiState.update { currentState ->
                    currentState.copy(selectedType = uiEvent.type)
                }
            }

            is TransFilterUiEvent.OnPreviewResultCount -> {
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
            transactionRepository.getAllTransactions()
                .collect { transactions ->
                    _uiState.value = _uiState.value.copy(transactions = transactions)
                }
        }
    }

    fun searchTransactionsByName(query: String) {
        viewModelScope.launch {
            transactionRepository.searchTransactionsByName(query)
                .collect { transactions ->
                    _uiState.value =
                        _uiState.value.copy(transactions = transactions)
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
            transactionRepository.getFilteredTransactions(
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

    fun removeTypeFilter() = uiState.value.appliedFilters?.let {
        applyUserFilters(null, it.sortOrder, it.startDate, it.endDate)
        _filterUiState.value = _filterUiState.value.copy(selectedType = null)
    }

    fun removeSortFilter() = uiState.value.appliedFilters?.let {
        applyUserFilters(it.type, null, it.startDate, it.endDate)
        _filterUiState.value = _filterUiState.value.copy(selectedSortOrder = null)
    }

    fun removeDateFilter() = uiState.value.appliedFilters?.let {
        applyUserFilters(it.type, it.sortOrder, null, null)
        _filterUiState.value = _filterUiState.value.copy(startDate = null, endDate = null)
    }

    fun previewFilterResultCount(
        type: TransactionType?,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?
    ) {
        viewModelScope.launch {
            val count = transactionRepository.countFilteredTransactions(
                type?.name,
                startDate,
                endDate
            )
            _filterUiState.value = _filterUiState.value.copy(count)
        }
    }

    fun markAsPaid(transactionId: Long) {
        viewModelScope.launch {
            transactionRepository.markTransactionAsPaid(transactionId)

            uiState.value.appliedFilters?.let {
                applyUserFilters(
                    type = it.type,
                    sortOrder = it.sortOrder,
                    startDate = it.startDate,
                    endDate = it.endDate
                )
            } ?: getAllTransactions()
        }
    }
}