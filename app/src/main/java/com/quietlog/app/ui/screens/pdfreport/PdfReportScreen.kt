package com.quietlog.app.ui.screens.pdfreport

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.quietlog.app.ui.components.PlaceholderScreen

@Composable
fun PdfReportScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(title = "PDF-отчёт для врача", modifier = modifier) {
        Text("Локальная генерация PDF, без отправки данных на сервер")
    }
}
