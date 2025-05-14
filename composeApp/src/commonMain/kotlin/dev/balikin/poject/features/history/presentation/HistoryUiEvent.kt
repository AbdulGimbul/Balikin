package dev.balikin.poject.features.history.presentation

sealed class HistoryUiEvent {
    data object OnRemoveType: HistoryUiEvent()
    data object OnRemoveDate: HistoryUiEvent()
    data object OnRemoveSort: HistoryUiEvent()
}