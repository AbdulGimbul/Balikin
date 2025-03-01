package dev.balikin.poject.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(uiEvent: LoginUiEvent) {
        when (uiEvent) {
            is LoginUiEvent.UsernameChanged -> {
                _uiState.value = _uiState.value.copy(username = uiEvent.username)
            }

            is LoginUiEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(password = uiEvent.password)
            }

            is LoginUiEvent.OnRemember -> {
                _uiState.value = _uiState.value.copy(isRemember = uiEvent.isRemember)
            }

            is LoginUiEvent.Login -> {
            }
        }
    }
}