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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.balikin_logo
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import dev.balikin.poject.ui.components.DefaultButton
import dev.balikin.poject.ui.components.DefaultTextField
import dev.balikin.poject.ui.components.LoginRegisterRow
import dev.balikin.poject.ui.components.OrDivider
import dev.balikin.poject.ui.components.WithGoogleButton
import dev.balikin.poject.ui.navigation.Screen
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.secondary_text
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.loginSuccess) {
        // This will navigate to Home after a successful login
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    if (uiState.showWebView) {
        LoginWebView(viewModel)
    } else {
        Login(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            moveToRegister = {
                navController.navigate(Screen.Register.route)
            }
        )
    }
}

@Composable
fun Login(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    moveToRegister: () -> Unit
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
            onClick = { onEvent(LoginUiEvent.OnGoogleLogin) },
            buttonText = "Continue with Google"
        )
        LoginRegisterRow(
            actionClick = { moveToRegister() },
            text = "Belum mempunyai akun? ",
            textAction = "Create account",
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
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
            style = MaterialTheme.typography.bodyMedium,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginWebView(viewModel: LoginViewModel) {
    val webViewState = rememberWebViewState(url = viewModel.loginUrl)
    val navigator = rememberWebViewNavigator()
    var isHandlingLogin by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        webViewState.webSettings.customUserAgentString =
            "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36"
        onDispose { }
    }

    LaunchedEffect(webViewState.loadingState) {
        if (webViewState.loadingState is LoadingState.Finished && !isHandlingLogin) {
            delay(300)
            navigator.evaluateJavaScript("document.documentElement.outerHTML") { rawHtml ->
                if (!rawHtml.isNullOrBlank() && rawHtml.contains("status") && rawHtml.contains("token")) {
                    isHandlingLogin = true
                    viewModel.handleLoginResponse(rawHtml)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login with Google") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.hideWebView() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) {
        WebView(
            state = webViewState,
            modifier = Modifier.padding(it).fillMaxSize(),
            navigator = navigator,
        )
    }
}