package dev.balikin.poject.features.auth.presentation.reset_password

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        startTimer()
    }

    fun onEvent(event: ResetPasswordUiEvent) {
        when (event) {
            is ResetPasswordUiEvent.OtpValueChanged -> {
                val newOtpValues = _uiState.value.otpValues.toMutableList()
                newOtpValues[event.index] = event.value
                _uiState.update { it.copy(otpValues = newOtpValues) }
            }

            is ResetPasswordUiEvent.VerifyOtp -> {
                // Implement OTP verification logic
                val otpValues = _uiState.value.otpValues
                if (otpValues.all { it.isNotEmpty() }) {
                    val otp = otpValues.joinToString("")
                    // Example - would actually call repository/API here
                    verifyOtp(otp)
                }
            }

            is ResetPasswordUiEvent.ResendCode -> {
                // Implement code resend logic
                _uiState.update { it.copy(timeLeft = 81) }
                startTimer()
            }
        }
    }

    private fun verifyOtp(otp: String) {
        _uiState.update { it.copy(isLoading = true) }

        coroutineScope.launch {
            delay(1000)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun startTimer() {
        coroutineScope.launch {
            while (_uiState.value.timeLeft > 0) {
                delay(1000)
                _uiState.update { it.copy(timeLeft = it.timeLeft - 1) }
            }
        }
    }
}

