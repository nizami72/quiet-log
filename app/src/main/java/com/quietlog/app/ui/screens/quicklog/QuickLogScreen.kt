package com.quietlog.app.ui.screens.quicklog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.quietlog.app.ui.components.PlaceholderScreen

@Composable
fun QuickLogScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(title = "Приступ сейчас", modifier = modifier) {
        Text("Шкала интенсивности 1-10 появится здесь")
    }
}
