package dev.balikin.poject.features.transaction.presentation

import com.tweener.alarmee.AlarmeeService

sealed class TransactionUiEvent {
    data object OnRemoveType : TransactionUiEvent()
    data object OnRemoveDate : TransactionUiEvent()
    data object OnRemoveSort : TransactionUiEvent()
    data class OnMarkAsPaid(val alarmeeService: AlarmeeService) : TransactionUiEvent()
    data class OnPaidClicked(val id: Long) : TransactionUiEvent()
    data object OnDismissDialog : TransactionUiEvent()
    data class OnQueryChanged(val query: String) : TransactionUiEvent()
}