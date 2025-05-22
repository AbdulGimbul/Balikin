package dev.balikin.poject.features.auth.presentation.reset_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.mail
import dev.balikin.poject.ui.components.BackToLogin
import dev.balikin.poject.ui.components.DefaultButton
import dev.balikin.poject.ui.navigation.Screen
import dev.balikin.poject.ui.theme.grey
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.secondary_text
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun ResetPasswordScreen(viewModel: ResetPasswordViewModel, navController: NavController) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ResetPassword(
        uiState = uiState.value,
        onEvent = viewModel::onEvent,
        navigateToLogin = {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        },
        navigateToSetNewPassword = {
            navController.navigate(Screen.SetNewPassword.route)
        }
    )
}

@Composable
fun ResetPassword(
    uiState: ResetPasswordUiState,
    onEvent: (ResetPasswordUiEvent) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToSetNewPassword: () -> Unit
) {
    val otpLength = 6

    val focusManager = LocalFocusManager.current
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

    LaunchedEffect(Unit) {
        delay(100)
        focusRequesters.firstOrNull()?.requestFocus()
    }

    val minutes = uiState.timeLeft / 60
    val seconds = uiState.timeLeft % 60
    val formattedTime =
        "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    val isOtpComplete = uiState.otpValues.all { it.isNotEmpty() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.mail),
            contentDescription = null,
            modifier = Modifier.size(56.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = primary_text
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = secondary_text)) {
                    append("Kami telah mengirimkan kode verifikasi ke email ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = primary_text)) {
                    append(uiState.email)
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            (0 until otpLength).forEach { index ->
                val focusRequester = focusRequesters[index]
                var isFocused by remember { mutableStateOf(false) }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(50.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp,
                            color = if (isFocused) primary_blue else grey,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    BasicTextField(
                        value = uiState.otpValues[index],
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || (newValue.length <= 1 && newValue.all { it.isDigit() })) {
                                val oldValue = uiState.otpValues[index]
                                onEvent(ResetPasswordUiEvent.OtpValueChanged(index, newValue))

                                if (newValue.isNotEmpty() && oldValue.isEmpty() && index < otpLength - 1) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                if (index < otpLength - 1) {
                                    focusRequesters[index + 1].requestFocus()
                                } else {
                                    focusManager.clearFocus()
                                }
                            }
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                innerTextField()
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isFocused = it.isFocused
                                // Select all text when focused to make replacing easy
                                if (it.isFocused && uiState.otpValues[index].isNotEmpty()) {
                                    // No direct select-all, but the single char will be replaced
                                }
                            }
                            .onKeyEvent { keyEvent ->
                                if (keyEvent.key == Key.Backspace) {
                                    if (uiState.otpValues[index].isEmpty() && index > 0) {
                                        // Move to previous field on backspace if current field is empty
                                        focusRequesters[index - 1].requestFocus()
                                        return@onKeyEvent true
                                    } else if (uiState.otpValues[index].isNotEmpty()) {
                                        // Clear current field
                                        onEvent(ResetPasswordUiEvent.OtpValueChanged(index, ""))
                                        // If now empty and not the first field, move to previous
                                        if (index > 0) {
                                            focusRequesters[index - 1].requestFocus()
                                        }
                                        return@onKeyEvent true
                                    }
                                }
                                false
                            }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        DefaultButton(
            text = "Continue",
            onClick = {
                onEvent(ResetPasswordUiEvent.VerifyOtp)
                if (isOtpComplete) {
                    navigateToSetNewPassword()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isOtpComplete,
            colors = ButtonDefaults.buttonColors(
                containerColor = primary_blue,
                contentColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.timeLeft > 0) {
            Text(
                text = "Resend code in $formattedTime",
                style = MaterialTheme.typography.bodyMedium.copy(color = secondary_text)
            )
        } else {
            Text(
                text = "Resend code",
                style = MaterialTheme.typography.bodyMedium.copy(color = secondary_text),
                modifier = Modifier.clickable {
                    onEvent(ResetPasswordUiEvent.ResendCode)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        BackToLogin(onBackClick = { navigateToLogin() })
    }
}
