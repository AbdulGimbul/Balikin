package dev.balikin.poject.features.home.presentation

import dev.balikin.poject.features.transaction.data.TransactionEntity
import dev.balikin.poject.ui.components.TransactionData

data class HomeUiState(
    val totalAmount: Double = 0.0,
    val latestTransactions: List<TransactionEntity> = emptyList(),
    val selectedTab: String = "Utang",
    val permissionError: String? = null,
    val showConfirmationDialog: Boolean = false,
    val pendingTransactionData: TransactionData? = null
)
