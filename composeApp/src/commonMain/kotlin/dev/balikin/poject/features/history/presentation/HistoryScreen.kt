package dev.balikin.poject.features.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.balikin.poject.ui.components.FilterButton
import dev.balikin.poject.ui.components.FilterTags
import dev.balikin.poject.ui.components.TransactionItem
import dev.balikin.poject.ui.navigation.Screen
import dev.balikin.poject.ui.theme.primary_text
import dev.balikin.poject.ui.theme.secondary_text

@Composable
fun HistoryScreen(viewModel: HistoryViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    History(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        moveToFilter = {
            navController.navigate(Screen.FilterHistory.route)
        }
    )
}

@Composable
fun History(
    uiState: HistoryUiState,
    onEvent: (HistoryUiEvent) -> Unit,
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

            Spacer(modifier = Modifier.height(24.dp))

            uiState.appliedFilters?.let { filters ->
                FilterTags(
                    filters = filters,
                    onRemoveDate = { onEvent(HistoryUiEvent.OnRemoveDate) },
                    onRemoveSort = { onEvent(HistoryUiEvent.OnRemoveSort) },
                    onRemoveType = { onEvent(HistoryUiEvent.OnRemoveType) }
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.transactions) { trans ->
                    TransactionItem(trans)
                }
            }
        }
    }
}

