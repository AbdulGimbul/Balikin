package dev.balikin.poject.utils

interface BrowserHelper {
    fun openBrowser(url: String)
}

expect fun getBrowserHelper(): BrowserHelper