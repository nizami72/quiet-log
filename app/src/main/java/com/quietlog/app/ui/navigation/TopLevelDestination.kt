package com.quietlog.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.quietlog.app.R

enum class TopLevelDestination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    HOME(Destination.Home.route, R.string.nav_home, Icons.Filled.Home),
    HISTORY(Destination.History.route, R.string.nav_history, Icons.Filled.History),
    INSIGHTS(Destination.Insights.route, R.string.nav_insights, Icons.Filled.Insights),
    SETTINGS(Destination.Settings.route, R.string.nav_settings, Icons.Filled.Settings),
}
