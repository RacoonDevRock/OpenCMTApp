package com.cmt.openapp.core.navigation

import androidx.compose.material3.Typography
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
    onThemeChange: (Int) -> Unit,
    onTypographyChange: (Typography) -> Unit,
    onFirstLaunchComplete: () -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        addHomeRoute(modifier, navController, onThemeChange, onTypographyChange, onFirstLaunchComplete)
        addResearchRoute(modifier, navController, onThemeChange, onTypographyChange)
        addDetailIncidentRoute(modifier, navController, onThemeChange, onTypographyChange)
        addReportRoute(modifier, navController, onThemeChange, onTypographyChange)
    }
}

fun NavGraphBuilder.addHomeRoute(
    modifier: Modifier, navController: NavHostController,
    onThemeChange: (Int) -> Unit,
    onTypographyChange: (Typography) -> Unit,
    onFirstLaunchComplete: () -> Unit
) {
    composable(Routes.HomeScreen.route) {
        HomeScreen(
            modifier = modifier,
            navigationController = navController,
            onThemeChange = onThemeChange,
            onTypographyChange = onTypographyChange,
            onFirstLaunchComplete = onFirstLaunchComplete
        )
    }
}

fun NavGraphBuilder.addResearchRoute(
    modifier: Modifier,
    navController: NavHostController,
    onThemeChange: (Int) -> Unit,
    onTypographyChange: (Typography) -> Unit,
) {
    composable(Routes.ResearchScreen.route) {
        ResearchScreen(
            modifier = modifier,
            navigationController = navController,
            onThemeChange = onThemeChange,
            onTypographyChange = onTypographyChange
        )
    }
}

fun NavGraphBuilder.addDetailIncidentRoute(
    modifier: Modifier,
    navController: NavHostController,
    onThemeChange: (Int) -> Unit,
    onTypographyChange: (Typography) -> Unit,
) {
    composable(
        route = "${Routes.DetailIncidentScreen.route}/{incidentId}",
        arguments = listOf(navArgument("incidentId") { type = NavType.StringType })
    ) { backStackEntry ->
        val incidentId = backStackEntry.arguments?.getString("incidentId") ?: ""
        DetailIncidentScreen(
            modifier = modifier,
            navigationController = navController,
            incidentId = incidentId,
            onThemeChange = onThemeChange,
            onTypographyChange = onTypographyChange
        )
    }
}

fun NavGraphBuilder.addReportRoute(
    modifier: Modifier,
    navController: NavHostController,
    onThemeChange: (Int) -> Unit,
    onTypographyChange: (Typography) -> Unit,
) {
    composable(
        route = "${Routes.ReportScreen.route}/{incidentId}",
        arguments = listOf(navArgument("incidentId") { type = NavType.IntType })
    ) { backStackEntry ->
        val incidentId = backStackEntry.arguments?.getInt("incidentId") ?: 0
        ReportScreen(
            modifier = modifier,
            navigationController = navController,
            incidentId = incidentId,
            onThemeChange = onThemeChange,
            onTypographyChange = onTypographyChange
        )
    }
}
