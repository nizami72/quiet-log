package com.quietlog.app.ui.navigation

sealed class Destination(val route: String) {
    data object Onboarding : Destination("onboarding")
    data object Home : Destination("home")
    data object QuickLog : Destination("quick_log")
    data object LogDetails : Destination("log_details")
    data object History : Destination("history")

    data object AttackRecord : Destination("attack_record/{attackId}") {
        const val ARG_ATTACK_ID = "attackId"
        fun createRoute(attackId: Long) = "attack_record/$attackId"
    }

    data object Insights : Destination("insights")
    data object Medications : Destination("medications")
    data object Settings : Destination("settings")
    data object Premium : Destination("premium")
    data object PdfReport : Destination("pdf_report")
}
