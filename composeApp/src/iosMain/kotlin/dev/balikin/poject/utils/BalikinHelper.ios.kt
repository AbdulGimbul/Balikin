package dev.balikin.poject.utils

import platform.Foundation.NSNumberFormatterCurrencyStyle

class IosBrowserHelper : BrowserHelper {
    override fun openBrowser(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}

actual fun getBrowserHelper(): BrowserHelper = IosBrowserHelper()

actual fun currencyFormat(
    amount: Double
): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        locale = NSLocale("in_ID")
    }
    return formatter.stringFromNumber(amount) ?: "$amount"
}