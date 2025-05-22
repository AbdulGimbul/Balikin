package dev.balikin.poject

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import dev.balikin.poject.ui.navigation.SetupNavHost
import dev.balikin.poject.ui.theme.ManropeTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme(
        typography = ManropeTypography()
    ) {
        val navController = rememberNavController()

        SetupNavHost(navController = navController)
    }
}