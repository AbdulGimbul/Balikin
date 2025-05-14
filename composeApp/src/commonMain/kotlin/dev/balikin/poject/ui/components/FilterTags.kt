package dev.balikin.poject.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import balikin.composeapp.generated.resources.Res
import balikin.composeapp.generated.resources.rm_tag
import dev.balikin.poject.features.transaction.presentation.FilterParameters
import dev.balikin.poject.utils.formatDate
import org.jetbrains.compose.resources.painterResource

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