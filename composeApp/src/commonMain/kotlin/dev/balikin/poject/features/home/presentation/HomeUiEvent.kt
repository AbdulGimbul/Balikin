package dev.balikin.poject.features.home.presentation

import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.ui.components.TransactionData

sealed class HomeUiEvent {
    data class OnToggleSelected(val type: String) : HomeUiEvent()
    data class LoadTotalAmountByType(val type: TransactionType) : HomeUiEvent()
    object LoadLatestTransactions : HomeUiEvent()
    data class ShowConfirmationDialog(val transactionData: TransactionData) : HomeUiEvent()
    object DismissConfirmationDialog : HomeUiEvent()
    object ConfirmTransaction : HomeUiEvent()
}
