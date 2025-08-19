package dev.balikin.poject.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.features.transaction.domain.UnifiedTransaction
import dev.balikin.poject.ui.theme.green
import dev.balikin.poject.ui.theme.red
import dev.balikin.poject.utils.currencyFormat
import dev.balikin.poject.utils.formatDate

@Composable
fun TransactionItem(transaction: UnifiedTransaction, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            AvatarImage(
                name = transaction.name,
                modifier = Modifier.clip(CircleShape),
                size = 40.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = formatDate(transaction.date),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                val noteText = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append("Note: ")
                    }
                    append(transaction.note)
                }
                if (transaction.note.isNotBlank()) {
                    Text(
                        text = noteText,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = currencyFormat(transaction.amount),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = transaction.type.name,
                    color = if (transaction.type == TransactionType.Utang) red else green,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }
}