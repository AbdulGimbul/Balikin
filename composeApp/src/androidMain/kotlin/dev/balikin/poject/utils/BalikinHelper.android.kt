package dev.balikin.poject.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import dev.balikin.poject.MyApplication
import java.text.NumberFormat
import java.util.Locale

class AndroidBrowserHelper(private val context: Context) : BrowserHelper {
    override fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}

actual fun getBrowserHelper(): BrowserHelper = AndroidBrowserHelper(MyApplication.appContext)

actual fun currencyFormat(
    amount: Double
): String {
    val indonesiaLocale = Locale("in", "ID")
    val format = NumberFormat.getCurrencyInstance(indonesiaLocale)
    return format.format(amount)
}
