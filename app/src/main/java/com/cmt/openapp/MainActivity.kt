package com.cmt.openapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Typography
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
import com.cmt.openapp.ui.theme.LargeTypography
import com.cmt.openapp.ui.theme.MediumTypography
import com.cmt.openapp.ui.theme.NormalTypography
import com.cmt.openapp.ui.theme.OpenAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)

        val isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", true)
        val savedTypography = sharedPreferences.getString("text_size", "normal")
        val savedTheme =
            sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(savedTheme)

        setContent {
            var isDarkTheme by remember { mutableStateOf(savedTheme == AppCompatDelegate.MODE_NIGHT_YES) }
            var currentTypography by remember {
                mutableStateOf(
                    when (savedTypography) {
                        "large" -> LargeTypography
                        "medium" -> MediumTypography
                        else -> NormalTypography
                    }
                )
            }

            val startDestination =
                if (isFirstLaunch) Routes.HomeScreen.route else Routes.ResearchScreen.route

            OpenAppTheme(darkTheme = isDarkTheme, typography = currentTypography) {
                CheckInternetScreen {
                    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                        val navigationController = rememberNavController()
                        AppNavGraph(
                            Modifier.padding(innerPadding),
                            navigationController,
                            startDestination,
                            onThemeChange = { newTheme ->
                                isDarkTheme = newTheme == AppCompatDelegate.MODE_NIGHT_YES
                                updateThemePreference(newTheme)
                            },
                            onTypographyChange = { newTypography ->
                                currentTypography = newTypography
                                updateTypographyPreference(newTypography)
                            },
                            onFirstLaunchComplete = {
                                sharedPreferences.edit()
                                    .putBoolean("is_first_launch", false)
                                    .apply()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun updateTypographyPreference(newTypography: Typography) {
        val typographyKey = when (newTypography) {
            LargeTypography -> "large"
            MediumTypography -> "medium"
            else -> "normal"
        }
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("text_size", typographyKey).apply()
    }

    private fun updateThemePreference(newTheme: Int) {
        AppCompatDelegate.setDefaultNightMode(newTheme)
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putInt("theme_mode", newTheme).apply()
    }
}

@Preview(name = "Open App Preview", showSystemUi = true)
@Composable
fun OpenAppPreview() {
}