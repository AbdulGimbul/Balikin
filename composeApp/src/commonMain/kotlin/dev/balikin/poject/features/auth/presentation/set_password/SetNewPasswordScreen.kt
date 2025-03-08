package dev.balikin.poject.features.auth.presentation.set_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.keyboard
import dev.balikin.poject.ui.components.BackToLogin
import dev.balikin.poject.ui.components.DefaultButton
import dev.balikin.poject.ui.components.DefaultTextField
import dev.balikin.poject.ui.navigation.Screen
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.red
import dev.balikin.poject.ui.theme.secondary_text
import org.jetbrains.compose.resources.painterResource

@Composable
fun SetNewPasswordScreen(
    viewModel: SetNewPasswordViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SetNewPassword(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        navigateToLogin = {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    )
}

@Composable
fun SetNewPassword(
    uiState: SetNewPasswordUiState,
    onEvent: (SetNewPasswordUiEvent) -> Unit,
    navigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.keyboard), contentDescription = null,
            modifier = Modifier.size(56.dp).padding(bottom = 16.dp)
        )
        Text(
            text = "Atur password baru", modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = primary_text
            )
        )
        Text(
            text = "Masukkan password baru",
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.bodyMedium.copy(color = secondary_text)
        )

        DefaultTextField(
            value = uiState.password,
            onValueChange = { onEvent(SetNewPasswordUiEvent.PasswordChanged(it)) },
            placehoder = "Password baru",
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp),
            isPassword = true,
        )

        DefaultTextField(
            value = uiState.confirmPassword,
            onValueChange = { onEvent(SetNewPasswordUiEvent.ConfirmPasswordChanged(it)) },
            placehoder = "Ketik ulang password baru",
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, top = 8.dp),
            isPassword = true,
        )

        if (uiState.passwordMismatch) {
            Text(
                text = "Password tidak sama. Mohon periksa kembali.",
                color = red,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        DefaultButton(
            text = "Reset password",
            onClick = { onEvent(SetNewPasswordUiEvent.ResetPassword) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primary_blue,
                contentColor = Color.White
            ),
            enabled = !uiState.isLoading && !uiState.passwordMismatch &&
                    uiState.password.isNotEmpty() && uiState.confirmPassword.isNotEmpty()
        )

        BackToLogin(onBackClick = {
            navigateToLogin()
        })
    }
}
