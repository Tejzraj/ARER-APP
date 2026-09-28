package com.arer.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arer.app.ui.auth.LoginScreen
import com.arer.app.ui.dashboard.DashboardScreen
import com.arer.app.ui.entry.MonthlyEntryScreen
import com.arer.app.ui.history.HistoryScreen
import com.arer.app.ui.reports.ReportsScreen
import com.arer.app.ui.settings.SettingsScreen
import com.arer.app.ui.setup.HmSetupScreen
import com.arer.app.ui.setup.MdmSetupScreen
import com.arer.app.ui.setup.SchoolSetupScreen
import com.arer.app.ui.splash.SplashScreen

@Composable
fun ArerNavGraph(isLoggedIn: Boolean = false) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.SchoolSetup.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.SchoolSetup.route) {
            SchoolSetupScreen(
                onNext = {
                    navController.navigate(Screen.HmSetup.route) {
                        popUpTo(Screen.SchoolSetup.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.HmSetup.route) {
            HmSetupScreen(
                onNext = {
                    navController.navigate(Screen.MdmSetup.route) {
                        popUpTo(Screen.HmSetup.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.MdmSetup.route) {
            MdmSetupScreen(
                onFinishSetup = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.MdmSetup.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToEntry = { navController.navigate(Screen.MonthlyEntry.route) },
                onNavigateToReports = { navController.navigate(Screen.Reports.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.MonthlyEntry.route) {
            MonthlyEntryScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Reports.route) {
            ReportsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.History.route) {
            HistoryScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLoggedOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
