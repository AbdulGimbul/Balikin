package dev.balikin.poject.features.transaction.presentation.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.balikin.poject.features.transaction.data.TransactionType
import dev.balikin.poject.utils.getCurrentDate
import dev.balikin.poject.utils.getLastWeekDate
import kotlinx.datetime.LocalDateTime

data class TransFilterUiState(
    val filterCount: Int = 0,
    val selectedType: TransactionType? = null,
    val selectedSortOrder: String = "asc",
    val startDate: LocalDateTime = getLastWeekDate(),
    val endDate: LocalDateTime = getCurrentDate()
)