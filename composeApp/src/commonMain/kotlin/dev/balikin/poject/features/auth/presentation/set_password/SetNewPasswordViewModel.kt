package dev.balikin.poject.features.auth.presentation.set_password

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SetNewPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SetNewPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SetNewPasswordUiEvent) {
        when (event) {
            is SetNewPasswordUiEvent.PasswordChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        password = event.password,
                        passwordMismatch = currentState.confirmPassword.isNotEmpty() &&
                                event.password != currentState.confirmPassword
                    )
                }
            }

            is SetNewPasswordUiEvent.ConfirmPasswordChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        confirmPassword = event.confirmPassword,
                        passwordMismatch = currentState.password.isNotEmpty() &&
                                currentState.password != event.confirmPassword
                    )
                }
            }

            is SetNewPasswordUiEvent.ResetPassword -> {
                // Validate passwords match
                if (_uiState.value.password != _uiState.value.confirmPassword) {
                    _uiState.update { it.copy(passwordMismatch = true) }
                    return
                }

                // TODO: Implement password reset logic
                _uiState.update { it.copy(isLoading = true) }
            }

        }
    }
}

