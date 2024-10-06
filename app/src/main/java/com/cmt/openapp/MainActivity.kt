package com.cmt.openapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cmt.openapp.model.Routes
import com.cmt.openapp.model.SearchViewModel
import com.cmt.openapp.screen.DetailIncidentScreen
import com.cmt.openapp.screen.HomeScreen
import com.cmt.openapp.screen.RequestScreen
import com.cmt.openapp.screen.ResearchScreen
import com.cmt.openapp.ui.theme.OpenAppTheme

class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenAppTheme {
                Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                    val navigationController = rememberNavController()
                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.HomeScreen.route
                    ) {
                        composable(Routes.HomeScreen.route) {
                            HomeScreen(
                                Modifier.padding(
                                    innerPadding
                                ), navigationController
                            )
                        }
                        composable(Routes.ResearchScreen.route) {
                            ResearchScreen(
                                Modifier.padding(
                                    innerPadding
                                ),
                                searchViewModel,
                                navigationController
                            )
                        }
                        composable(Routes.DetailIncidentScreen.route) {
                            DetailIncidentScreen(
                                Modifier.padding(
                                    innerPadding
                                ),
                                navigationController
                            )
                        }
                        composable(Routes.RequestScreen.route) {
                            RequestScreen(
                                Modifier.padding(
                                    innerPadding
                                ),
                                navigationController
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview(name = "Home Screen Preview", showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    OpenAppTheme {
    }
}