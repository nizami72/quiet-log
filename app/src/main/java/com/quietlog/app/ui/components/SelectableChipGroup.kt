package com.quietlog.app.ui.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * @param options stable storage key paired with its localized display label.
 * @param selected the set of selected storage keys.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SelectableChipGroup(
    options: List<Pair<String, String>>,
    selected: Set<String>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(modifier = modifier) {
        options.forEach { (key, label) ->
            FilterChip(
                modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                selected = key in selected,
                onClick = { onToggle(key) },
                label = { Text(label) },
            )
        }
    }
}
