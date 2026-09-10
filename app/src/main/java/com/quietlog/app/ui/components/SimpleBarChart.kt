package com.quietlog.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.quietlog.app.ui.StatsBucket

@Composable
fun SimpleBarChart(
    points: List<StatsBucket>,
    modifier: Modifier = Modifier,
    barAreaHeight: Dp = 120.dp,
) {
    val maxCount = (points.maxOfOrNull { it.count } ?: 1).coerceAtLeast(1)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        points.forEach { point ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 4.dp),
            ) {
                Text(
                    text = "%.1f".format(point.avgIntensity),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Box(
                    modifier = Modifier.height(barAreaHeight),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    val fraction = point.count.toFloat() / maxCount
                    Box(
                        modifier = Modifier
                            .height((barAreaHeight * fraction).coerceAtLeast(4.dp))
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
                            ),
                    )
                }
                Text(
                    text = point.count.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = point.label,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
