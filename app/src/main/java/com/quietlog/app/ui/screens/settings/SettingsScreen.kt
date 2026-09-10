package com.quietlog.app.ui.screens.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.quietlog.app.ui.components.PlaceholderScreen

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(title = "Настройки", modifier = modifier) {
        Text("Тема, единицы измерения, напоминания, управление подпиской")
    }
}
