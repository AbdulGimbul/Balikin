package dev.balikin.poject

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import dev.balikin.poject.features.front_page.presentation.OnBoardingScreen
import dev.balikin.poject.features.front_page.presentation.OnBoardingViewModel
import dev.balikin.poject.ui.theme.ManropeTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var showOnboarding by rememberSaveable { mutableStateOf(true) }

    MaterialTheme(
        typography = ManropeTypography()
    ) {
        if (showOnboarding) {
            OnBoardingScreen(viewModel = OnBoardingViewModel())
        } else {
            // Your main app content
            Text("Main App Content")
        }
    }
}