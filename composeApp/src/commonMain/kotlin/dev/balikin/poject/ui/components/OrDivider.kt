package dev.balikin.poject.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.balikin.poject.ui.theme.grey
import dev.balikin.poject.ui.theme.secondary_text

@Composable
fun OrDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .padding(end = 8.dp),
            color = grey
        )

        Text(
            text = "or",
            color = secondary_text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .padding(start = 8.dp),
            color = grey
        )
    }
}