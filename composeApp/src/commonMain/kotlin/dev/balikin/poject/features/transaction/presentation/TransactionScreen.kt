package dev.balikin.poject.features.transaction.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.agus
import balikin.composeapp.generated.resources.rm_tag
import balikin.composeapp.generated.resources.trans_piutang
import dev.balikin.poject.features.transaction.data.TransactionEntity
import dev.balikin.poject.ui.components.FilterButton
import dev.balikin.poject.ui.navigation.Screen
import dev.balikin.poject.ui.theme.grey2
import dev.balikin.poject.ui.theme.orange
import dev.balikin.poject.ui.theme.primary_blue
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.secondary_text
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
fun FilterTags(
    filters: FilterParameters,
    onRemoveType: () -> Unit,
    onRemoveSort: () -> Unit,
    onRemoveDate: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        filters.type?.let { type ->
            FilterChip(
                label = { Text(type.name, style = MaterialTheme.typography.labelSmall) },
                selected = true,
                onClick = onRemoveType,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.rm_tag),
                        contentDescription = null
                    )
                }
            )
        }
        filters.sortOrder?.let { sort ->
            FilterChip(
                label = {
                    Text(
                        if (sort == "asc") "Terkecil" else "Terbesar",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = true,
                onClick = onRemoveSort,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.rm_tag),
                        contentDescription = null
                    )
                },
            )
        }

        if (filters.startDate != null && filters.endDate != null) {
            FilterChip(
                label = {
                    Text(
                        text = "${formatDate(filters.startDate)} - ${formatDate(filters.endDate)}",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = true,
                onClick = onRemoveDate,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.rm_tag),
                        contentDescription = null
                    )
                },
            )
        }
    }
}

@Composable
fun BillCard(
    transaction: TransactionEntity,
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

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = onTagihClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (transaction.type.name.lowercase() == "piutang") orange.copy(
                            alpha = 0.15f
                        ) else primary_blue.copy(alpha = 0.15f),
                        contentColor = if (transaction.type.name.lowercase() == "piutang") orange else primary_blue
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (transaction.type.name.lowercase() == "piutang") "Tagih →" else "Bayar →",
                        modifier = Modifier.padding(horizontal = 4.dp)
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
                            text = "Piutang",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = grey2
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

                    Image(
                        painter = painterResource(Res.drawable.trans_piutang),
                        contentDescription = "Refresh",
                    )
                }
            }
        }
    }
}