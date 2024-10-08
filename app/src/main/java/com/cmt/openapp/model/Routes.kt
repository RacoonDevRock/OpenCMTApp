package com.cmt.openapp.model

sealed class Routes(val route: String) {
    object HomeScreen : Routes("homeScreen")
    object ResearchScreen : Routes("researchScreen")
    object DetailIncidentScreen : Routes("detailIncidentScreen")
    object ReportScreen : Routes("reportScreen")
}