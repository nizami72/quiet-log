package com.quietlog.app.ui.screens.attackrecord

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.quietlog.app.ui.components.PlaceholderScreen

@Composable
fun AttackRecordScreen(
    attackId: Long,
    modifier: Modifier = Modifier,
) {
    PlaceholderScreen(title = "Приступ #$attackId", modifier = modifier) {
        Text("Просмотр/редактирование/удаление записи без пейволлов")
    }
}
