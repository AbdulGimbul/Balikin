package dev.balikin.poject.utils

class IosBrowserHelper : BrowserHelper {
    override fun openBrowser(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}

actual fun getBrowserHelper(): BrowserHelper = IosBrowserHelper()