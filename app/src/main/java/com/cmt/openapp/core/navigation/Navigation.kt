package com.cmt.openapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cmt.openapp.core.ui.home.HomeScreen
import com.cmt.openapp.detail.ui.DetailIncidentScreen
import com.cmt.openapp.report.ui.ReportScreen
import com.cmt.openapp.research.ui.ResearchScreen

@Composable
fun AppNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String = Routes.HomeScreen.route,
    onThemeChange: (Int) -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        addHomeRoute(modifier, navController)
        addResearchRoute(modifier, navController, onThemeChange)
        addDetailIncidentRoute(modifier, navController, onThemeChange)
        addReportRoute(modifier, navController, onThemeChange)
    }
}

fun NavGraphBuilder.addHomeRoute(modifier: Modifier, navController: NavHostController) {
    composable(Routes.HomeScreen.route) {
        HomeScreen(modifier = modifier, navigationController = navController)
    }
}

fun NavGraphBuilder.addResearchRoute(modifier: Modifier, navController: NavHostController, onThemeChange: (Int) -> Unit) {
    composable(Routes.ResearchScreen.route) {
        ResearchScreen(modifier = modifier, navigationController = navController, onThemeChange = onThemeChange)
    }
}

fun NavGraphBuilder.addDetailIncidentRoute(modifier: Modifier, navController: NavHostController, onThemeChange: (Int) -> Unit) {
    composable(
        route = "${Routes.DetailIncidentScreen.route}/{incidentId}",
        arguments = listOf(navArgument("incidentId") { type = NavType.StringType })
    ) { backStackEntry ->
        val incidentId = backStackEntry.arguments?.getString("incidentId") ?: ""
        DetailIncidentScreen(modifier = modifier, navigationController = navController, incidentId = incidentId, onThemeChange = onThemeChange)
    }
}

fun NavGraphBuilder.addReportRoute(modifier: Modifier, navController: NavHostController, onThemeChange: (Int) -> Unit) {
    composable(
        route = "${Routes.ReportScreen.route}/{incidentId}",
        arguments = listOf(navArgument("incidentId") { type = NavType.LongType })
    ) { backStackEntry ->
        val incidentId = backStackEntry.arguments?.getLong("incidentId") ?: 0L
        ReportScreen(modifier = modifier, navigationController = navController, incidentId = incidentId, onThemeChange = onThemeChange)
    }
}
