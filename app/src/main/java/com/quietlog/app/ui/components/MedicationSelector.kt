package com.quietlog.app.ui.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.quietlog.app.data.local.entity.MedicationEntity

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MedicationSelector(
    medications: List<MedicationEntity>,
    selectedIds: Set<Long>,
    onToggle: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (medications.isEmpty()) {
        Text(
            "Список медикаментов пуст — добавьте в Настройки → Медикаменты",
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier,
        )
        return
    }

    FlowRow(modifier = modifier) {
        medications.forEach { medication ->
            FilterChip(
                modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                selected = medication.id in selectedIds,
                onClick = { onToggle(medication.id) },
                label = { Text("${medication.name} ${medication.dosage}".trim()) },
            )
        }
    }
}
