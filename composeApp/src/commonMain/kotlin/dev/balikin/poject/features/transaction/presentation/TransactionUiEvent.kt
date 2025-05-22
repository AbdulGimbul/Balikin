package dev.balikin.poject.features.transaction.presentation

sealed class TransactionUiEvent {
    data object OnRemoveType: TransactionUiEvent()
    data object OnRemoveDate: TransactionUiEvent()
    data object OnRemoveSort: TransactionUiEvent()
    data object OnMarkAsPaid: TransactionUiEvent()
    data class OnPaidClicked(val id: Long): TransactionUiEvent()
    data object OnDismissDialog: TransactionUiEvent()
    data class OnQueryChanged(val query: String): TransactionUiEvent()
}