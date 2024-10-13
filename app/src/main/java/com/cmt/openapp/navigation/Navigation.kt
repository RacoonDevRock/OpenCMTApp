package com.cmt.openapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cmt.openapp.model.Routes
import com.cmt.openapp.ui.home.HomeScreen
import com.cmt.openapp.ui.incident.DetailIncidentScreen
import com.cmt.openapp.ui.report.ReportScreen
import com.cmt.openapp.ui.research.ResearchScreen

@Composable
fun AppNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String = Routes.ResearchScreen.route,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        addHomeRoute(modifier, navController)
        addResearchRoute(modifier, navController)
        addDetailIncidentRoute(modifier, navController)
        addReportRoute(modifier, navController)
    }
}

fun NavGraphBuilder.addHomeRoute(modifier: Modifier, navController: NavHostController) {
    composable(Routes.HomeScreen.route) {
        HomeScreen(modifier = modifier, navigationController = navController)
    }
}

fun NavGraphBuilder.addResearchRoute(modifier: Modifier, navController: NavHostController) {
    composable(Routes.ResearchScreen.route) {
        ResearchScreen(modifier = modifier, navigationController = navController)
    }
}

fun NavGraphBuilder.addDetailIncidentRoute(modifier: Modifier, navController: NavHostController) {
    composable(Routes.DetailIncidentScreen.route) {
        DetailIncidentScreen(modifier = modifier, navigationController = navController)
    }
}

fun NavGraphBuilder.addReportRoute(modifier: Modifier, navController: NavHostController) {
    composable(Routes.ReportScreen.route) {
        ReportScreen(modifier = modifier, navigationController = navController)
    }
}
