package dev.balikin.poject.features.history.presentation.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.balikin.poject.features.history.presentation.HistoryViewModel
import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.features.transaction.presentation.filter.TransFilterUiEvent
import dev.balikin.poject.ui.components.DateFilterChips
import dev.balikin.poject.ui.components.DefaultButton
import dev.balikin.poject.ui.components.FilterSection
import dev.balikin.poject.ui.theme.primary_blue

@Composable
fun HistoryFilterScreen(viewModel: HistoryViewModel, navController: NavController) {
    val uiState by viewModel.filterUiState.collectAsStateWithLifecycle()

    HistoryFilter(
        uiState = uiState,
        onEvent = viewModel::onEventFilter,
        onBackClick = { navController.popBackStack() }
    )
}

@Composable
fun HistoryFilter(
    onBackClick: () -> Unit,
    uiState: HistoryFilterUiState,
    onEvent: (HistoryFilterUiEvent) -> Unit
) {

    LaunchedEffect(uiState.selectedType, uiState.startDate, uiState.endDate) {
        onEvent(HistoryFilterUiEvent.OnPreviewResultCount)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Filter",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilterSection(
                title = "Jenis",
                filterOptions = listOf("Utang", "Piutang"),
                selectedOption = uiState.selectedType?.name,
                onOptionSelected = { option ->
                    onEvent(
                        HistoryFilterUiEvent.OnTypeChanged(
                            if (uiState.selectedType?.name == option) null else TransactionType.valueOf(
                                option
                            )
                        )
                    )

                }
            )
            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))

            val currentSort = when (uiState.selectedSortOrder) {
                "asc" -> "Terkecil"
                "desc" -> "Terbesar"
                else -> null
            }
            FilterSection(
                title = "Sort",
                filterOptions = listOf("Terkecil", "Terbesar"),
                selectedOption = currentSort,
                onOptionSelected = { option ->
                    val isCurrentlySelected = currentSort == option
                    val newSortOrder = when {
                        isCurrentlySelected -> null
                        option == "Terkecil" -> "asc"
                        option == "Terbesar" -> "desc"
                        else -> null
                    }
                    onEvent(HistoryFilterUiEvent.OnSortOrderChanged(newSortOrder))
                }
            )
            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Periode",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                )
                if (uiState.startDate != null || uiState.endDate != null) {
                    TextButton(
                        onClick = {
                            onEvent(HistoryFilterUiEvent.OnStartDateChanged(null))
                            onEvent(HistoryFilterUiEvent.OnEndDateChanged(null))
                        }
                    ) {
                        Text(
                            text = "Clear",
                            style = MaterialTheme.typography.titleMedium.copy(color = primary_blue)
                        )
                    }
                }
            }
            DateFilterChips(
                initStartDate = uiState.startDate,
                initEndDate = uiState.endDate,
                onStartDateChange = {
                    if (it != null) {
                        onEvent(HistoryFilterUiEvent.OnStartDateChanged(it))
                    }
                },
                onEndDateChange = {
                    if (it != null) {
                        onEvent(HistoryFilterUiEvent.OnEndDateChanged(it))
                    }
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            DefaultButton(
                text = "Show ${uiState.filterCount} result",
                onClick = {
                    onEvent(HistoryFilterUiEvent.OnApplyFilters)
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}