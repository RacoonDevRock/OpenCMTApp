package com.cmt.openapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val colorTextTitle = Color(0xFF000000)
val backgroundWhite = Color(0xFFFFFFFF)
val backgroundCMT = Color(0xFFE5E5E5)
val containerData = Color(0xFFD9D9D9)
val ButtonColor = Color(0xFF848688)

private val DarkColorScheme = darkColorScheme(
    background = backgroundCMT,
    primaryContainer = containerData,
    tertiary = ButtonColor,
    secondaryContainer = backgroundWhite,
    primary = colorTextTitle
)

private val LightColorScheme = lightColorScheme(
    background = backgroundCMT,
    primaryContainer = containerData,
    tertiary = ButtonColor,
    secondaryContainer = backgroundWhite,
    primary = colorTextTitle

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun OpenAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}