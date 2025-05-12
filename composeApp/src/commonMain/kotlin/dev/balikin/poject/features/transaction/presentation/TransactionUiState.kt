package dev.balikin.poject.features.transaction.presentation

import dev.balikin.poject.features.transaction.data.TransactionEntity

data class TransactionUiState(
    val transactions: List<TransactionEntity> = emptyList(),
)