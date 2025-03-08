package dev.balikin.poject.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.balikin.poject.ui.theme.primary_blue

@Composable
fun LoginRegisterRow(
    actionClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    textAction: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text)
        Text(
            text = textAction,
            color = primary_blue,
            modifier = Modifier.clickable { actionClick() }
        )
    }
}