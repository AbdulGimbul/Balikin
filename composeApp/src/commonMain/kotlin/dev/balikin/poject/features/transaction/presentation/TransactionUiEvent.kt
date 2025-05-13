package dev.balikin.poject.features.transaction.presentation

sealed class TransactionUiEvent {
    data object OnRemoveType: TransactionUiEvent()
    data object OnRemoveDate: TransactionUiEvent()
    data object OnRemoveSort: TransactionUiEvent()
}