package dev.balikin.poject.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.agus
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.secondary_text
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen() {
    val transactions = listOf(
        Transaction(
            name = "Ilham Ardiansyah",
            date = "17 Agustus ● 12.22 PM",
            note = "bekas bell baras Ng",
            amount = "Rp. 459.000",
            type = "Utang"
        ),
        Transaction(
            name = "Irsan Ramadhan",
            date = "17 Agustus ● 12.22 PM",
            note = "bekas bell baras Ng",
            amount = "Rp. 99.000",
            type = "Plutang"
        ),
        Transaction(
            name = "Winggar Waharjut",
            date = "17 Agustus ● 12.22 PM",
            note = "bekas bell baras Ng",
            amount = "Rp. 43.000",
            type = "Plutang"
        )
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(Res.drawable.agus), contentDescription = null,
                modifier = Modifier.size(40.dp).padding(end = 8.dp),
            )
            Column {
                Text(
                    text = "Hello, Fatamore",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = primary_text
                    )
                )
                Text(
                    text = "Kosongkan utangmu yak! ;)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = secondary_text
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    tint = primary_text
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        var selectedTab by remember { mutableStateOf("Utang") }
        DebtCard(
            totalDebt = "Rp. 897.900",
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        Spacer(Modifier.height(24.dp))

        // Recent Transactions
        Text(
            text = "Transaksi Terbaru",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(transactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}


@Composable
fun DebtCard(
    totalDebt: String,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("Utang", "Piutang")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        Color(0xFF5865F2),
                        Color(0xFF907EFD),
                        Color(0XFF926BFF)
                    )
                ),
                shape = RoundedCornerShape(10.dp)
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Total Utang",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = totalDebt,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(50)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tabs.forEach { tab ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(50))
                            .background(if (tab == selectedTab) Color(0xFF7B61FF) else Color.Transparent)
                            .clickable { onTabSelected(tab) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = if (tab == selectedTab) Color.White else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun TransactionItem(transaction: Transaction) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = transaction.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = transaction.date,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Note: ${transaction.note}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = transaction.amount,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = transaction.type,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

data class Transaction(
    val name: String,
    val date: String,
    val note: String,
    val amount: String,
    val type: String
)