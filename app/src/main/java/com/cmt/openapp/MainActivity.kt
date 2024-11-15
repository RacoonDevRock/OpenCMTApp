package com.cmt.openapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.cmt.openapp.core.navigation.AppNavGraph
import com.cmt.openapp.core.navigation.Routes
import com.cmt.openapp.core.network.CheckInternetScreen
import com.cmt.openapp.ui.theme.OpenAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val savedTheme =
            sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(savedTheme)

        setContent {
            var isDarkTheme by remember { mutableStateOf(savedTheme == AppCompatDelegate.MODE_NIGHT_YES) }

            OpenAppTheme(isDarkTheme) {
                CheckInternetScreen {
                    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                        val navigationController = rememberNavController()
                        AppNavGraph(
                            Modifier.padding(innerPadding),
                            navigationController,
                            Routes.HomeScreen.route,
                            onThemeChange = { newTheme ->
                                // Cambiar tema y actualizar el estado
                                isDarkTheme = newTheme == AppCompatDelegate.MODE_NIGHT_YES
                                updateThemePreference(newTheme)
                            })
                    }
                }
            }
        }
    }

    private fun updateThemePreference(newTheme: Int) {
        AppCompatDelegate.setDefaultNightMode(newTheme)
        val sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putInt("theme_mode", newTheme).apply()
    }
}

@Preview(name = "Open App Preview", showSystemUi = true)
@Composable
fun OpenAppPreview() {
}