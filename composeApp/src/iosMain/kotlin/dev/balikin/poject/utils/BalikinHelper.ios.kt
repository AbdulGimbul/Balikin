package dev.balikin.poject.utils

import com.tweener.alarmee.configuration.AlarmeeIosPlatformConfiguration
import com.tweener.alarmee.configuration.AlarmeePlatformConfiguration
import platform.Foundation.NSLocale
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

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
    return formatter.stringFromNumber(NSNumber(double = amount)) ?: "$amount"
}

actual fun createAlarmeePlatformConfiguration(): AlarmeePlatformConfiguration {
    return AlarmeeIosPlatformConfiguration
}