package dev.balikin.poject.utils

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.tweener.alarmee.channel.AlarmeeNotificationChannel
import com.tweener.alarmee.configuration.AlarmeeAndroidPlatformConfiguration
import com.tweener.alarmee.configuration.AlarmeePlatformConfiguration
import dev.balikin.poject.MyApplication
import dev.balikin.poject.R
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

actual fun createAlarmeePlatformConfiguration(): AlarmeePlatformConfiguration {
    return AlarmeeAndroidPlatformConfiguration(
        notificationIconResId = R.drawable.ic_launcher_foreground,
        notificationChannels = listOf(
            AlarmeeNotificationChannel(
                id = "due_date_reminders",
                name = "Due Date Reminders",
                importance = NotificationManager.IMPORTANCE_HIGH
            )
        )
    )
}