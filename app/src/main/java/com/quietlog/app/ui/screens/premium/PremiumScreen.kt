package com.quietlog.app.ui.screens.premium

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.quietlog.app.ui.components.PlaceholderScreen

@Composable
fun PremiumScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(title = "QuietLog Premium", modifier = modifier) {
        Text("Разовая покупка: PDF-отчёт, расширенная аналитика, цикл, тренды")
    }
}
