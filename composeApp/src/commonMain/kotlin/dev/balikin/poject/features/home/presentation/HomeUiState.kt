package dev.balikin.poject.features.home.presentation

import dev.balikin.poject.features.transaction.data.TransactionEntity

data class HomeUiState(
    val totalAmount: Double = 0.0,
    val latestTransactions: List<TransactionEntity> = emptyList(),
    val selectedTab: String = "Utang"
)