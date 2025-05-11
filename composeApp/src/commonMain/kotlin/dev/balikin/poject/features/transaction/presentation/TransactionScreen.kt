package dev.balikin.poject.features.transaction.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.agus
import balikin.composeapp.generated.resources.trans_piutang
import dev.balikin.poject.ui.components.FilterButton
import dev.balikin.poject.ui.theme.grey2
import dev.balikin.poject.ui.theme.primary
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.secondary_text
import org.jetbrains.compose.resources.painterResource

@Composable
fun TransactionScreen() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Transaksi Utang Piutang",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 24.dp),
            textAlign = TextAlign.Center
        )
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.bodyMedium,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    placeholder = { Text(text = "Search", color = secondary_text) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = primary_text,
                        unfocusedLabelColor = secondary_text,
                    ),
                    modifier = Modifier
                        .weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterButton(onClick = {})
            }
            LazyColumn(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                items(10) {
                    BillCard(
                        name = "Winggar Waharjut",
                        date = "17 Agustus",
                        time = "12.22 PM",
                        piutangAmount = "Rp.87.000",
                        dueDate = "22 Dec 2025",
                        onTagihClick = { /* TODO: Handle tagih action */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BillCard(
    name: String,
    date: String,
    time: String,
    piutangAmount: String,
    dueDate: String,
    onTagihClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.agus),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "$date ● $time",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = secondary_text
                        )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onTagihClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFECE5),
                        contentColor = Color(0xFFFF7A00)
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = "Tagih →")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "Piutang",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = grey2
                            )
                        )
                        Text(
                            text = piutangAmount,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.5f))

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Due Date",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = grey2
                            )
                        )
                        Text(
                            text = dueDate,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(Res.drawable.trans_piutang),
                        contentDescription = "Refresh",
                    )
                }
            }
        }
    }
}