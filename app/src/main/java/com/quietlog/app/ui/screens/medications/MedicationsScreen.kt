package com.quietlog.app.ui.screens.medications

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.quietlog.app.ui.components.PlaceholderScreen

@Composable
fun MedicationsScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(title = "Медикаменты", modifier = modifier) {
        Text("Список препаратов пользователя появится здесь")
    }
}
