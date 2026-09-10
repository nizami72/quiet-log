package com.quietlog.app.ui.screens.logdetails

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.quietlog.app.ui.components.PlaceholderScreen

@Composable
fun LogDetailsScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(title = "Добавить детали", modifier = modifier) {
        Text("Локализация, симптомы, триггеры, медикаменты, заметка")
    }
}
