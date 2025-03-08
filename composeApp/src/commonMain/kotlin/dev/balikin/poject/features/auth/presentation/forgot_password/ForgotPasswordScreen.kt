package dev.balikin.poject.features.auth.presentation.forgot_password

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.solar_key
import dev.balikin.poject.ui.components.BackToLogin
import dev.balikin.poject.ui.components.DefaultButton
import dev.balikin.poject.ui.components.DefaultTextField
import dev.balikin.poject.ui.navigation.Screen
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.secondary_text
import org.jetbrains.compose.resources.painterResource

@Composable
fun ForgotPasswordScreen(viewModel: ForgotPasswordViewModel, navController: NavController) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ForgotPassword(
        uiState = uiState.value,
        onEvent = viewModel::onEvent,
        navigateToLogin = {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    )
}

@Composable
fun ForgotPassword(
    uiState: ForgotPasswordUiState,
    onEvent: (ForgotPasswordUiEvent) -> Unit,
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
            painter = painterResource(Res.drawable.solar_key), contentDescription = null,
            modifier = Modifier.size(56.dp).padding(bottom = 16.dp)
        )
        Text(
            text = "Lupa password?", modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = primary_text
            )
        )
        Text(
            text = "Reset password akan dikirim melalui email",
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.bodyMedium.copy(color = secondary_text)
        )
        DefaultTextField(
            value = uiState.email,
            onValueChange = { onEvent(ForgotPasswordUiEvent.EmailChanged(it)) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            placehoder = "Alamat email"
        )
        DefaultButton(
            text = "Reset password",
            onClick = { onEvent(ForgotPasswordUiEvent.ResetPassword) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primary_blue,
                contentColor = Color.White
            ),
            enabled = !uiState.isLoading
        )
        BackToLogin(onBackClick = {
            onEvent(ForgotPasswordUiEvent.BackToLogin)
            navigateToLogin()
        })
    }
}
