package dev.balikin.poject.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.balikin.poject.features.auth.data.AuthRepository
import dev.balikin.poject.features.auth.domain.LoginApiModel
import dev.balikin.poject.network.onError
import dev.balikin.poject.network.onSuccess
import dev.balikin.poject.storage.SessionHandler
import io.ktor.http.decodeURLPart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.math.log

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionHandler: SessionHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    val loginUrl = "https://balikin.vercel.app/api/v1/auth/google" // Your backend's Google auth URL
    val redirectUrlScheme = "yourappscheme"

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

            is LoginUiEvent.OnGoogleLogin -> {
                _uiState.value = _uiState.value.copy(showWebView = true)
            }
        }
    }

    /**
     * Handles the JSON content retrieved from the WebView.
     */
    fun handleLoginResponse(jsonContent: String) {
        println("Cek 2 ya: $jsonContent")

        viewModelScope.launch {
            try {
                // Step 1: Clean the raw string from the JavaScript callback
                val cleanedHtml = jsonContent
                    .removeSurrounding("\"")      // Remove the leading/trailing quotes
                    .replace("\\u003C", "<") // Un-escape '<'
                    .replace("\\u003E", ">") // Un-escape '>'
                    .replace("\\\"", "\"")   // Un-escape '\"'

                println("Cleaned HTML: $cleanedHtml")

                // Step 2: Extract the JSON content from between the <pre> tags
                val jsonContent = cleanedHtml.substringAfter("<pre>").substringBefore("</pre>")

                if (jsonContent.isBlank()) {
                    throw IllegalStateException("Extracted JSON content is blank.")
                }

                println("Extracted JSON: $jsonContent")

                // Step 3: Parse the clean JSON string
                val loginResponse = Json { ignoreUnknownKeys = true }.decodeFromString<LoginApiModel>(jsonContent)
                val userData = loginResponse.data

                // Save user data using SessionHandler
                sessionHandler.setUserData(
                    username = userData.email,
                    nama = userData.name,
                    role = userData.roleId.toString(),
                    namaToko = "", // Or from response if available
                    alamat = "",   // Or from response if available
                    telp = "",     // Or from response if available
                    token = loginResponse.token
                )

                // Update the state to indicate success, triggering navigation
                _uiState.update { it.copy(loginSuccess = true, showWebView = false) }
                println("Cek 3 ya: ${loginResponse.token}")
            } catch (e: Exception) {
                println("Cek 4 ya Failed to parse login response: ${e.message}")
                _uiState.update { it.copy(showWebView = false, errorMessage = "Login failed.") }
            }
        }
    }

    fun hideWebView() {
        _uiState.update { it.copy(showWebView = false) }
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