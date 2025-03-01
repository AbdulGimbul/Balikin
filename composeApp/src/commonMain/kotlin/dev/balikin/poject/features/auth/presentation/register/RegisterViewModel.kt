package dev.balikin.poject.features.auth.presentation.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(uiEvent: RegisterUiEvent) {
        when (uiEvent) {
            is RegisterUiEvent.UsernameChanged -> {
                _uiState.value = _uiState.value.copy(username = uiEvent.username)
            }

            is RegisterUiEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(password = uiEvent.password)
            }

            is RegisterUiEvent.RepeatedPasswordChanged -> {
                _uiState.value = _uiState.value.copy(repeatedPassword = uiEvent.repeatedPassword)
            }

            is RegisterUiEvent.OnTermsAccepted -> {
                _uiState.value = _uiState.value.copy(termsAccepted = uiEvent.isAccepted)
            }

            is RegisterUiEvent.Submit -> {
            }
        }
    }
}