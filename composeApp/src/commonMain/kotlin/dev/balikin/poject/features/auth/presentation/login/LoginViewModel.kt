package dev.balikin.poject.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.auth.data.AuthRepository
import dev.balikin.poject.network.onError
import dev.balikin.poject.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

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
                login()
            }
        }
    }

    private fun login(){
        viewModelScope.launch(Dispatchers.IO) {

            val result = authRepository.loginWithGoogle()
            withContext(Dispatchers.Main){
                result.onSuccess {
                    println("Yes login sukses dengan data: $it")
                }.onError {
                    println("Oops login gagal dengan pesan: $it")
                }
            }
        }
    }
}