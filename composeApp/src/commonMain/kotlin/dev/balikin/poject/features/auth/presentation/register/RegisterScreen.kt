package dev.balikin.poject.features.auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.balikin_logo
import dev.balikin.poject.ui.components.DefaultButton
import dev.balikin.poject.ui.components.DefaultTextField
import dev.balikin.poject.ui.components.LoginRegisterRow
import dev.balikin.poject.ui.components.OrDivider
import dev.balikin.poject.ui.components.WithGoogleButton
import dev.balikin.poject.ui.navigation.Screen
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.secondary_text
import org.jetbrains.compose.resources.painterResource

@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navController: NavController) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Register(
        uiState = uiState.value,
        onEvent = viewModel::onEvent,
        moveToLogin = {
            navController.navigate(Screen.Login.route)
        }
    )
}

@Composable
fun Register(
    uiState: RegisterUiState,
    onEvent: (RegisterUiEvent) -> Unit,
    moveToLogin: () -> Unit
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
            "Buat akun untuk menikmati semua fitur dari Balikin.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            color = secondary_text,
            textAlign = TextAlign.Center
        )
        DefaultTextField(
            value = uiState.username,
            onValueChange = { onEvent(RegisterUiEvent.UsernameChanged(it)) },
            modifier = Modifier.padding(vertical = 8.dp),
            placehoder = "Alamat email"
        )
        DefaultTextField(
            value = uiState.password,
            onValueChange = { onEvent(RegisterUiEvent.PasswordChanged(it)) },
            modifier = Modifier.padding(vertical = 8.dp),
            placehoder = "Buat password",
            isPassword = true
        )
        DefaultTextField(
            value = uiState.repeatedPassword,
            onValueChange = { onEvent(RegisterUiEvent.RepeatedPasswordChanged(it)) },
            modifier = Modifier.padding(vertical = 8.dp),
            placehoder = "Ketik ulang password",
            isPassword = true
        )

        TermsAndCondition(
            termsAccepted = uiState.termsAccepted,
            openTermsOfService = ::openTermsOfService,
            openPrivacyPolicy = ::openPrivacyPolicy,
            onClick = { onEvent(RegisterUiEvent.OnTermsAccepted(it)) }
        )

        DefaultButton(
            text = "Register",
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = primary_blue,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OrDivider()
        WithGoogleButton(
            onClick = {}
        )
        LoginRegisterRow(
            actionClick = { moveToLogin() },
            text = "Sudah mempunyai akun? ",
            textAction = "Sign in",
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        )
    }
}

@Composable
fun TermsAndCondition(
    termsAccepted: Boolean,
    openTermsOfService: () -> Unit,
    openPrivacyPolicy: () -> Unit,
    onClick: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = termsAccepted,
            onCheckedChange = { onClick(it) }
        )

        val annotatedString = buildAnnotatedString {
            append("By signing up, you agree to our ")

            // Terms of Services (blue and clickable)
            pushStringAnnotation(tag = "TERMS", annotation = "terms_click")
            pushStyle(SpanStyle(color = primary_blue))
            append("Terms of Services")
            pop() // pop style
            pop() // pop annotation

            append(" and ")

            // Privacy Policy (blue and clickable)
            pushStringAnnotation(tag = "PRIVACY", annotation = "privacy_click")
            pushStyle(SpanStyle(color = primary_blue))
            append("Privacy Policy")
            pop() // pop style
            pop() // pop annotation
        }

        var textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

        BasicText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium.copy(color = secondary_text),
            onTextLayout = { layoutResult ->
                textLayoutResult.value = layoutResult
            },
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { offset ->
                    textLayoutResult.value?.let { layoutResult ->
                        val clickedOffset = layoutResult.getOffsetForPosition(offset)
                        annotatedString.getStringAnnotations(
                            start = clickedOffset,
                            end = clickedOffset
                        )
                            .firstOrNull()?.let { annotation ->
                                when (annotation.tag) {
                                    "TERMS" -> openTermsOfService()
                                    "PRIVACY" -> openPrivacyPolicy()
                                }
                            }
                    }
                }
            }
        )
    }
}

@Composable
fun EnhancedLoading(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = primary_blue)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Mohon tunggu...")
        }
    }
}

fun openTermsOfService() {
    println("Terms of Service clicked")
}

fun openPrivacyPolicy() {
    println("Privacy Policy clicked")
}