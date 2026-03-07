package dev.balikin.poject

import androidx.compose.ui.window.ComposeUIViewController
import dev.balikin.poject.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App(onExitApp = {}) }