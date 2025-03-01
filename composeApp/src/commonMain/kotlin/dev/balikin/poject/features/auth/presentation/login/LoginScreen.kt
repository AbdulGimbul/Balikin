package dev.balikin.poject.features.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.balikin_logo
import dev.balikin.poject.ui.components.DefaultButton
import dev.balikin.poject.ui.components.DefaultTextField
import dev.balikin.poject.ui.components.LoginRegisterRow
import dev.balikin.poject.ui.components.OrDivider
import dev.balikin.poject.ui.components.WithGoogleButton
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.secondary_text
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Login(
        uiState = uiState.value,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun Login(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .padding(18.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.balikin_logo),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                alignment = Alignment.Center
            )
        }
        Text(
            "Login untuk menikmati semua fitur dari Balikin.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            color = secondary_text,
            textAlign = TextAlign.Center
        )
        DefaultTextField(
            value = uiState.username,
            onValueChange = { onEvent(LoginUiEvent.UsernameChanged(it)) },
            modifier = Modifier.padding(vertical = 8.dp),
            placehoder = "Alamat email"
        )
        DefaultTextField(
            value = uiState.password,
            onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
            modifier = Modifier.padding(vertical = 8.dp),
            placehoder = "Buat password",
            isPassword = true
        )
        RememberMe(
            isRemember = uiState.isRemember,
            onClick = { onEvent(LoginUiEvent.OnRemember(it)) }
        )

        DefaultButton(
            text = "Login",
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = primary_blue,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OrDivider()
        WithGoogleButton(
            onClick = {},
            buttonText = "Continue with Google"
        )
        LoginRegisterRow(
            onSignInClick = {},
            text = "Belum mempunyai akun? ",
            textAction = "Create account",
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
    }
}

@Composable
fun RememberMe(
    isRemember: Boolean,
    onClick: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isRemember,
            onCheckedChange = { onClick(it) }
        )

        Text(
            text = "Remember me",
            color = secondary_text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Forgot password",
            color = primary_blue,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            modifier = Modifier.clickable {}
        )
    }
}