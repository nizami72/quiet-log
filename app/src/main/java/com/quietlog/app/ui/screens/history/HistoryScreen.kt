package com.quietlog.app.ui.screens.history

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.quietlog.app.ui.components.PlaceholderScreen

@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(title = "История приступов", modifier = modifier) {
        Text("Лента приступов с фильтром по периоду появится здесь")
    }
}
