package dev.balikin.poject.features.transaction.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.transaction.data.TransactionRepository
import dev.balikin.poject.features.transaction.data.TransactionType
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

    private val defaultStartDate = getLastWeekDate()
    private val defaultEndDate = getCurrentDate()

    private val _filterPreviewCount = MutableStateFlow(0)
    val filterPreviewCount = _filterPreviewCount.asStateFlow()

    init {
        applyDefaultFilters()
    }

    fun onEvent(uiEvent: TransactionUiEvent){
        when (uiEvent) {
            is TransactionUiEvent.OnRemoveDate -> removeDateFilter()
            is TransactionUiEvent.OnRemoveSort -> removeSortFilter()
            is TransactionUiEvent.OnRemoveType -> removeTypeFilter()
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
            transactionRepository.getFilteredTransactions(
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

    fun removeTypeFilter() = uiState.value.appliedFilters?.let {
        applyUserFilters(null, it.sortOrder, it.startDate, it.endDate)
    }

    fun removeSortFilter() = uiState.value.appliedFilters?.let {
        applyUserFilters(it.type, null, it.startDate, it.endDate)
    }

    fun removeDateFilter() = uiState.value.appliedFilters?.let {
        applyUserFilters(it.type, it.sortOrder, null, null)
    }

    fun previewFilterResultCount(
        type: TransactionType?,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ) {
        viewModelScope.launch {
            val count = transactionRepository.countFilteredTransactions(
                type?.name,
                startDate,
                endDate
            )
            _filterPreviewCount.value = count
        }
    }
}