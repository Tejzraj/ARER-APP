package com.arer.app.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SchoolSetup : Screen("school_setup")
    object HmSetup : Screen("hm_setup")
    object MdmSetup : Screen("mdm_setup")
    object Dashboard : Screen("dashboard")
    object MonthlyEntry : Screen("monthly_entry")
    object Reports : Screen("reports")
    object History : Screen("history")
    object Settings : Screen("settings")
}
