package dev.balikin.poject.features.auth.presentation.forgot_password

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForgotPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(uiEvent: ForgotPasswordUiEvent) {
        when (uiEvent) {
            is ForgotPasswordUiEvent.EmailChanged -> {
                _uiState.value = _uiState.value.copy(email = uiEvent.email)
            }

            is ForgotPasswordUiEvent.ResetPassword -> {
                _uiState.value = _uiState.value.copy(isLoading = true)
            }

            is ForgotPasswordUiEvent.BackToLogin -> {
            }
        }
    }
}

