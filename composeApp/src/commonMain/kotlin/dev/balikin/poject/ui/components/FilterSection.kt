package dev.balikin.poject.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import dev.balikin.poject.ui.theme.primary
import dev.balikin.poject.ui.theme.red
import dev.balikin.poject.utils.formatDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import multiplatform.network.cmptoast.ToastDuration
import multiplatform.network.cmptoast.showToast

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
    initStartDate: LocalDateTime?,
    initEndDate: LocalDateTime?,
    onStartDateChange: (LocalDateTime?) -> Unit,
    onEndDateChange: (LocalDateTime?) -> Unit
) {
    var startDate by remember { mutableStateOf<LocalDateTime?>(initStartDate) }
    var endDate by remember { mutableStateOf<LocalDateTime?>(initEndDate) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showValidationError by remember { mutableStateOf(false) }

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

        if (showValidationError) {
            showToast(
                message = "Tanggal akhir tidak boleh kurang dari tanggal awal",
                backgroundColor = red,
                duration = ToastDuration.Long
            )
        }
    }

    if (showStartDatePicker) {
        CustomDatePickerDialog(
            isEndDate = false,
            onDateSelected = {
                showStartDatePicker = false

                if (endDate != null && endDate!! < it) {
                    showValidationError = true
                } else {
                    onStartDateChange(it)
                    showValidationError = false
                }
            },
            onDismiss = { showStartDatePicker = false }
        )
    }
    if (showEndDatePicker) {
        CustomDatePickerDialog(
            isEndDate = true,
            onDateSelected = {
                showEndDatePicker = false

                if (startDate != null && it < startDate!!) {
                    showValidationError = true
                } else {
                    onEndDateChange(it)
                    showValidationError = false
                }
            },
            onDismiss = { showEndDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    isEndDate: Boolean = false,
    onDateSelected: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
    )
    val selectedDate = datePickerState.selectedDateMillis?.let {
        Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                selectedDate?.let { date ->
                    val resultDateTime = if (isEndDate) {
                        // End of the day: 23:59
                        LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, 23, 59)
                    } else {
                        // Start of the day: 00:00
                        LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, 0, 0)
                    }
                    onDateSelected(resultDateTime)
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