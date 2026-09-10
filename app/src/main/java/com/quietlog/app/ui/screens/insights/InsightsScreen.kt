package com.quietlog.app.ui.screens.insights

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.quietlog.app.ui.components.PlaceholderScreen

@Composable
fun InsightsScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(title = "Статистика / Инсайты", modifier = modifier) {
        Text("Барометрическая корреляция и базовая статистика — бесплатно")
    }
}
