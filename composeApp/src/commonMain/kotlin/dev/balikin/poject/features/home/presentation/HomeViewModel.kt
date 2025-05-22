package dev.balikin.poject.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.transaction.data.TransactionEntity
import dev.balikin.poject.features.transaction.data.TransactionRepository
import dev.balikin.poject.features.transaction.data.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomeViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getLatestTransactions()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnToggleSelected -> {
                _uiState.value = _uiState.value.copy(selectedTab = event.type)
            }

            is HomeUiEvent.LoadTotalAmountByType -> {
                getTotalAmountByType(event.type)
            }

            HomeUiEvent.LoadLatestTransactions -> {
                getLatestTransactions()
            }
        }
    }

    fun getLatestTransactions() {
        viewModelScope.launch {
            transactionRepository.getAllTransactions()
                .collect { transactions ->
                    _uiState.value = _uiState.value.copy(latestTransactions = transactions)
                }
        }
    }

    fun getTotalAmountByType(type: TransactionType) {
        viewModelScope.launch {
            val totalAmount = transactionRepository.getTotalAmountByType(type) ?: 0.0
            _uiState.value = _uiState.value.copy(totalAmount = totalAmount)
        }
    }

    fun addTransaction(
        name: String,
        date: String,
        note: String,
        amount: String,
        type: String
    ) {
        val transactionType = when (type.lowercase()) {
            "utang" -> TransactionType.Utang
            "piutang" -> TransactionType.Piutang
            else -> TransactionType.Utang
        }

        viewModelScope.launch {
            val dueDate: LocalDateTime = try {
                LocalDateTime.parse(date)
            } catch (e: Exception) {
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            }

            val transaction = TransactionEntity(
                name = name,
                dueDate = dueDate,
                note = note,
                amount = amount.toDouble(),
                type = transactionType
            )
            transactionRepository.addTransaction(transaction)

            val currentUiType = when (_uiState.value.selectedTab.lowercase()) {
                "utang" -> TransactionType.Utang
                "piutang" -> TransactionType.Piutang
                else -> TransactionType.Piutang
            }

            getTotalAmountByType(currentUiType)
        }
    }
}