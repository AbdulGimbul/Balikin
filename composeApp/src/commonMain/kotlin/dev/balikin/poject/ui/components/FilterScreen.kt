package dev.balikin.poject.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.features.transaction.presentation.FilterParameters
import dev.balikin.poject.features.transaction.presentation.TransactionViewModel
import dev.balikin.poject.ui.theme.primary
import dev.balikin.poject.utils.formatDate
import dev.balikin.poject.utils.getCurrentDate
import dev.balikin.poject.utils.getLastWeekDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun FilterScreen(viewModel: TransactionViewModel, navController: NavController) {
    val previewCount by viewModel.filterPreviewCount.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        FilterHeader(onBackClick = { })
        FilterContent(
            filterCount = previewCount,
            onApplyFilters = { params ->
                viewModel.applyUserFilters(
                    type = params.type,
                    sortOrder = params.sortOrder,
                    startDate = params.startDate,
                    endDate = params.endDate
                )
                navController.popBackStack()
            },
            viewModel = viewModel
        )
    }
}

@Composable
fun FilterHeader(onBackClick: () -> Unit) {
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
}

@Composable
fun FilterContent(
    filterCount: Int,
    onApplyFilters: (FilterParameters) -> Unit,
    viewModel: TransactionViewModel
) {
    var selectedType by remember { mutableStateOf<TransactionType?>(null) }
    var selectedSortOrder by remember { mutableStateOf("asc") }
    var startDate by remember { mutableStateOf(getLastWeekDate()) }
    var endDate by remember { mutableStateOf(getCurrentDate()) }

    LaunchedEffect(selectedType, startDate, endDate) {
        viewModel.previewFilterResultCount(
            type = selectedType,
            startDate = startDate,
            endDate = endDate
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilterSection(
            title = "Jenis",
            filterOptions = listOf("Utang", "Piutang"),
            selectedOption = selectedType?.name,
            onOptionSelected = { option ->
                selectedType =
                    if (selectedType?.name == option) null else TransactionType.valueOf(option)
            }
        )
        HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
        FilterSection(
            title = "Sort",
            filterOptions = listOf("Terkecil", "Terbesar"),
            selectedOption = if (selectedSortOrder == "asc") "Terkecil" else "Terbesar",
            onOptionSelected = { option ->
                selectedSortOrder = if (option == "Terkecil") "asc" else "desc"
            }
        )
        HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
        Text(
            text = "Periode",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        )
        DateFilterChips(
            initStartDate = startDate,
            initEndDate = endDate,
            onStartDateChange = {
                if (it != null) {
                    startDate = it
                }
            },
            onEndDateChange = {
                if (it != null) {
                    endDate = it
                }
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        DefaultButton(
            text = "Show $filterCount result",
            onClick = {
                onApplyFilters(
                    FilterParameters(
                        selectedType,
                        selectedSortOrder,
                        startDate,
                        endDate
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun FilterSection(
    title: String,
    filterOptions: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            filterOptions.forEachIndexed { index, option ->
                FilterChip(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) },
                    label = { Text(option) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFilterChips(
    initStartDate: LocalDateTime,
    initEndDate: LocalDateTime,
    onStartDateChange: (LocalDateTime?) -> Unit,
    onEndDateChange: (LocalDateTime?) -> Unit
) {
    var startDate by remember { mutableStateOf<LocalDateTime?>(initStartDate) }
    var endDate by remember { mutableStateOf<LocalDateTime?>(initEndDate) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(initStartDate, initEndDate) {
        startDate = initStartDate
        endDate = initEndDate
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AssistChip(
            onClick = { showStartDatePicker = true },
            label = {
                Text(
                    text = startDate?.let { formatDate(it) } ?: "Start Date",
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Calendar",
                    tint = primary,
                    modifier = Modifier.size(18.dp)
                )
            },
            modifier = Modifier.weight(1f)
        )

        Text("s/d")

        AssistChip(
            onClick = { showEndDatePicker = true },
            label = {
                Text(
                    text = endDate?.let { formatDate(it) } ?: "End Date",
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Calendar",
                    tint = primary,
                    modifier = Modifier.size(18.dp)
                )
            },
            modifier = Modifier.weight(1f)
        )
    }

    if (showStartDatePicker) {
        CustomDatePickerDialog(
            onDateSelected = {
                onStartDateChange(it)
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false }
        )
    }
    if (showEndDatePicker) {
        CustomDatePickerDialog(
            onDateSelected = {
                onEndDateChange(it)
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    onDateSelected: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
    )
    val selectedDate = datePickerState.selectedDateMillis?.let {
        Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault())
    }
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                selectedDate?.let {
                    onDateSelected(it)
                }
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}