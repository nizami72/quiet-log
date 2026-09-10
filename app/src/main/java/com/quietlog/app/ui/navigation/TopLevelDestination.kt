package com.quietlog.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    HOME(Destination.Home.route, "Главная", Icons.Filled.Home),
    HISTORY(Destination.History.route, "История", Icons.Filled.History),
    INSIGHTS(Destination.Insights.route, "Статистика", Icons.Filled.Insights),
    SETTINGS(Destination.Settings.route, "Настройки", Icons.Filled.Settings),
}
