package dev.balikin.poject.features.transaction.presentation

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.agus
import balikin.composeapp.generated.resources.trans_piutang
import balikin.composeapp.generated.resources.trans_utang
import dev.balikin.poject.features.transaction.data.TransactionEntity
import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.ui.components.FilterButton
import dev.balikin.poject.ui.components.FilterTags
import dev.balikin.poject.ui.navigation.Screen
import dev.balikin.poject.ui.theme.green
import dev.balikin.poject.ui.theme.grey2
import dev.balikin.poject.ui.theme.orange
import dev.balikin.poject.ui.theme.primary
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.red
import dev.balikin.poject.ui.theme.secondary
import dev.balikin.poject.ui.theme.secondary_text
import dev.balikin.poject.ui.theme.stroke
import dev.balikin.poject.utils.currencyFormat
import dev.balikin.poject.utils.formatDate
import dev.balikin.poject.utils.formatDateCreated
import org.jetbrains.compose.resources.painterResource

@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Transaction(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        moveToFilter = {
            navController.navigate(Screen.FilterTrans.route)
        }
    )
}

@Composable
fun Transaction(
    uiState: TransactionUiState,
    onEvent: (TransactionUiEvent) -> Unit,
    moveToFilter: () -> Unit
) {

    if (uiState.showDialog && uiState.selectedTransactionId != null) {
        CustomDialog(
            onDismissRequest = { onEvent(TransactionUiEvent.OnDismissDialog) },
            onConfirmation = {
                onEvent(TransactionUiEvent.OnMarkAsPaid)
            }
        )
    }

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
                    value = uiState.nameSearch,
                    onValueChange = {
                        onEvent(TransactionUiEvent.OnQueryChanged(it))
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                        )
                    },
                    placeholder = { Text(text = "Search", color = secondary_text) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = primary_text,
                        unfocusedLabelColor = secondary_text,
                        focusedBorderColor = primary_blue
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterButton(onClick = moveToFilter)
            }
            // Display filter chips
            uiState.appliedFilters?.let { filters ->
                FilterTags(
                    filters = filters,
                    onRemoveDate = { onEvent(TransactionUiEvent.OnRemoveDate) },
                    onRemoveSort = { onEvent(TransactionUiEvent.OnRemoveSort) },
                    onRemoveType = { onEvent(TransactionUiEvent.OnRemoveType) }
                )
            }
            LazyColumn(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                items(uiState.transactions) { transaction ->
                    BillCard(
                        transaction = transaction,
                        onPaid = { onEvent(TransactionUiEvent.OnPaidClicked(transaction.id)) },
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
    transaction: TransactionEntity,
    onPaid: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.Top) {
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
                            text = transaction.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = formatDateCreated(transaction.createdAt),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = secondary_text
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Row {
                    val roundedShape = RoundedCornerShape(50)
                    // This will be implemented in online mode
//                    Text(
//                        text = if (transaction.type.name.lowercase() == "piutang") "Tagih →" else "Bayar →",
//                        modifier = Modifier
//                            .clip(roundedShape)
//                            .clickable { onPaid() }
//                            .background(
//                                color = if (transaction.type.name.lowercase() == "piutang")
//                                    orange.copy(alpha = 0.15f) else primary_blue.copy(alpha = 0.15f),
//                                shape = roundedShape
//                            )
//                            .padding(horizontal = 10.dp, vertical = 4.dp),
//                        color = if (transaction.type.name.lowercase() == "piutang") orange else primary_blue,
//                        style = MaterialTheme.typography.labelLarge
//                    )
                    Text(
                        text = "Selesai →",
                        modifier = Modifier
                            .clip(roundedShape)
                            .clickable { onPaid() }
                            .background(
                                color = green.copy(alpha = 0.15f),
                                shape = roundedShape
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        color = green,
                        style = MaterialTheme.typography.labelLarge
                    )
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
                            text = transaction.type.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (transaction.type == TransactionType.Utang) red else green
                            )
                        )
                        Text(
                            text = currencyFormat(transaction.amount.toDouble()),
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
                            text = formatDate(transaction.dueDate),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    val piutangPainter = painterResource(Res.drawable.trans_piutang)
                    val utangPainter = painterResource(Res.drawable.trans_utang)
                    Image(
                        painter = if (transaction.type.name.lowercase() == "piutang") piutangPainter else utangPainter,
                        contentDescription = "Refresh",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    tint = primary,
                )
                Text(
                    text = "Selesai?",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleMedium.copy(color = secondary, fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "Apakah anda yakin utang yang anda pinjamkan sudah terbayar?",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(color = secondary_text, textAlign = TextAlign.Center)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.weight(1f).padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = primary_blue
                        )
                    ) {
                        Text("Ya, Selesai")
                    }
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.weight(1f).padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = primary_text,
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text("Batal")
                    }
                }
            }
        }
    }
}