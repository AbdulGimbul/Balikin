package dev.balikin.poject.features.transaction.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.transaction.data.TransactionEntity
import dev.balikin.poject.features.transaction.data.TransactionRepository
import dev.balikin.poject.features.transaction.data.TransactionType
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalDateTime.Companion
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TransactionViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactions = mutableStateListOf<TransactionEntity>()

    init {
        viewModelScope.launch {
            transactionRepository.getAllTransactions().collect { newList ->
                _transactions.clear()
                _transactions.addAll(newList)
            }
        }
    }
}