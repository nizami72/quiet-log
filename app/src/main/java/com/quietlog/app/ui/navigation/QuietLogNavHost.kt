package com.quietlog.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quietlog.app.ui.screens.attackrecord.AttackRecordScreen
import com.quietlog.app.ui.screens.history.HistoryScreen
import com.quietlog.app.ui.screens.home.HomeScreen
import com.quietlog.app.ui.screens.insights.InsightsScreen
import com.quietlog.app.ui.screens.logdetails.LogDetailsScreen
import com.quietlog.app.ui.screens.medications.MedicationsScreen
import com.quietlog.app.ui.screens.onboarding.OnboardingScreen
import com.quietlog.app.ui.screens.pdfreport.PdfReportScreen
import com.quietlog.app.ui.screens.premium.PremiumScreen
import com.quietlog.app.ui.screens.quicklog.QuickLogScreen
import com.quietlog.app.ui.screens.settings.SettingsScreen

@Composable
fun QuietLogNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Home.route,
        modifier = modifier,
    ) {
        composable(Destination.Onboarding.route) { OnboardingScreen() }

        composable(Destination.Home.route) {
            HomeScreen(onLogAttackClick = { navController.navigate(Destination.QuickLog.route) })
        }

        composable(Destination.QuickLog.route) {
            QuickLogScreen(
                onSaved = { attackId ->
                    navController.navigate(Destination.LogDetails.createRoute(attackId)) {
                        popUpTo(Destination.Home.route) { inclusive = false }
                    }
                },
            )
        }

        composable(
            route = Destination.LogDetails.route,
            arguments = listOf(navArgument(Destination.LogDetails.ARG_ATTACK_ID) { type = NavType.LongType }),
        ) {
            LogDetailsScreen(onDone = { navController.popBackStack() })
        }

        composable(Destination.History.route) {
            HistoryScreen(
                onOpenAttack = { attackId ->
                    navController.navigate(Destination.AttackRecord.createRoute(attackId))
                },
            )
        }

        composable(
            route = Destination.AttackRecord.route,
            arguments = listOf(navArgument(Destination.AttackRecord.ARG_ATTACK_ID) { type = NavType.LongType }),
        ) { backStackEntry ->
            val attackId = backStackEntry.arguments?.getLong(Destination.AttackRecord.ARG_ATTACK_ID) ?: 0L
            AttackRecordScreen(attackId = attackId)
        }

        composable(Destination.Insights.route) { InsightsScreen() }
        composable(Destination.Medications.route) { MedicationsScreen() }

        composable(Destination.Settings.route) {
            SettingsScreen(
                onNavigateToMedications = { navController.navigate(Destination.Medications.route) },
                onNavigateToPremium = { navController.navigate(Destination.Premium.route) },
            )
        }

        composable(Destination.Premium.route) { PremiumScreen() }
        composable(Destination.PdfReport.route) { PdfReportScreen() }
    }
}
