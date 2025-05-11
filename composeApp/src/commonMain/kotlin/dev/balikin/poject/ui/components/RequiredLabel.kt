package dev.balikin.poject.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

/**
 * A label component that shows a required field indicator (red asterisk)
 */
@Composable
fun RequiredLabel(label: String) {
    // Use an AnnotatedString to color only the asterisk
    Text(
        buildAnnotatedString {
            append(label)
            append(" ")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                append("*")
            }
        }
    )
}

