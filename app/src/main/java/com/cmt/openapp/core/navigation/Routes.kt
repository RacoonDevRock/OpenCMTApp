package com.cmt.openapp.core.navigation

sealed class Routes(val route: String) {
    object HomeScreen : Routes("homeScreen")
    object ResearchScreen : Routes("researchScreen")
    object DetailIncidentScreen : Routes("detailIncidentScreen") {
        fun createRoute(incidentId: String) = "detailIncidentScreen/$incidentId"
    }
    object ReportScreen : Routes("reportScreen") {
        fun createRoute(incidentId: Int) = "reportScreen/$incidentId"
    }
}