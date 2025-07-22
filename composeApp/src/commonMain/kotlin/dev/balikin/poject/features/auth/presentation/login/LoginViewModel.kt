package dev.balikin.poject.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.auth.data.AuthRepository
import dev.balikin.poject.features.auth.domain.LoginApiModel
import dev.balikin.poject.network.onError
import dev.balikin.poject.network.onSuccess
import dev.balikin.poject.storage.SessionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionHandler: SessionHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.NotAuthenticated())
    val uiState = _uiState.asStateFlow()

    val loginUrl = "https://balikin.vercel.app/api/v1/auth/google"

    init {
        checkTokenValidity()
    }

    fun onEvent(uiEvent: LoginUiEvent) {
        when (uiEvent) {
            is LoginUiEvent.UsernameChanged -> {
                updateState { it.copy(username = uiEvent.username) }
            }

            is LoginUiEvent.PasswordChanged -> {
                updateState { it.copy(password = uiEvent.password) }
            }

            is LoginUiEvent.OnRemember -> {
                updateState { it.copy(isRemember = uiEvent.isRemember) }
            }

            is LoginUiEvent.Login -> {
                login()
            }

            is LoginUiEvent.OnGoogleLogin -> {
                updateState { it.copy(showWebView = true) }
            }
        }
    }

    private fun updateState(update: (LoginUiState.NotAuthenticated) -> LoginUiState.NotAuthenticated) {
        _uiState.value =
            (_uiState.value as? LoginUiState.NotAuthenticated)?.let(update) ?: _uiState.value
    }

    fun handleLoginResponse(jsonContent: String) {

        viewModelScope.launch {
            try {
                val cleanedHtml = jsonContent
                    .removeSurrounding("\"")
                    .replace("\\u003C", "<")
                    .replace("\\u003E", ">")
                    .replace("\\\"", "\"")

                println("Cleaned HTML: $cleanedHtml")

                val jsonContent = cleanedHtml.substringAfter("<pre>").substringBefore("</pre>")

                if (jsonContent.isBlank()) {
                    throw IllegalStateException("Extracted JSON content is blank.")
                }

                println("Extracted JSON: $jsonContent")

                val loginResponse =
                    Json { ignoreUnknownKeys = true }.decodeFromString<LoginApiModel>(jsonContent)
                val userData = loginResponse.data

                sessionHandler.setUserData(
                    email = userData.email,
                    nama = userData.name,
                    token = loginResponse.token
                )

                _uiState.value = LoginUiState.Authenticated
            } catch (e: Exception) {
                updateState { it.copy(showWebView = false, errorMessage = "Login failed.") }
            }
        }
    }

    fun hideWebView() {
        updateState { it.copy(showWebView = false) }
    }

    private fun checkTokenValidity() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.isTokenValid("", "10", "0")
            result.onSuccess {
                _uiState.value = LoginUiState.Authenticated
            }.onError { error ->
                updateState {
                    it.copy(errorMessage = error.message)
                }
            }

            updateState { it.copy(isLoading = false) }
        }
    }

    private fun login() {
        viewModelScope.launch(Dispatchers.IO) {

            val result = authRepository.loginWithGoogle()
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    println("Yes login sukses dengan data: $it")
                }.onError {
                    println("Oops login gagal dengan pesan: $it")
                }
            }
        }
    }
}