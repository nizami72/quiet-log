package com.quietlog.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun IntensityPicker(
    selected: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(10) { index ->
            val intensity = index + 1
            val label: @Composable RowScope.() -> Unit = {
                Text(
                    text = intensity.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
            }
            if (intensity == selected) {
                Button(
                    enabled = enabled,
                    onClick = { onSelect(intensity) },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.aspectRatio(1f),
                    content = label,
                )
            } else {
                OutlinedButton(
                    enabled = enabled,
                    onClick = { onSelect(intensity) },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.aspectRatio(1f),
                    content = label,
                )
            }
        }
    }
}
