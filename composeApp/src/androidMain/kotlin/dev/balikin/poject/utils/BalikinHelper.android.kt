package dev.balikin.poject.utils

import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.core.uri.Uri
import dev.balikin.poject.MyApplication

class AndroidBrowserHelper(private val context: Context) : BrowserHelper {
    override fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}

actual fun getBrowserHelper(): BrowserHelper = AndroidBrowserHelper(MyApplication.appContext)