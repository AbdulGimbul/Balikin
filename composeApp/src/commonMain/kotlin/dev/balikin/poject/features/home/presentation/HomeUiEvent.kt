package dev.balikin.poject.features.home.presentation

import dev.balikin.poject.features.transaction.data.TransactionType

sealed class HomeUiEvent {
    data class OnToggleSelected(val type: String) : HomeUiEvent()
    data class LoadTotalAmountByType(val type: TransactionType) : HomeUiEvent()
    object LoadLatestTransactions : HomeUiEvent()
}