package com.cmt.openapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// COLOR TEXT
val colorTextDark = Color(0xFFFFFFFF)
val colorTextLight = Color(0xFF000000)

// COLOR TEXT BUTTON
val colorTextButtonDark = Color(0xFFFFFFFF)

// COLOR BUTTON
val colorButtonDark = Color(0xFF252525)
val colorButtonLight= Color(0xFF848688)

// COLOR TEXT BUTTON DISABLED
val colorTextButtonDisableDark = Color(0xFFB3B3B3)
val colorTextButtonDisableLight = Color(0xFFCACACA)

// COLOR BUTTON DISABLED
val colorButtonDisableDark = Color(0xFF444444)
val colorButtonDisableLight= Color(0xFFAFB1B3)

// COLOR ICON
val colorIconDark = Color(0xFFFFFFFF)
val colorIconLight = Color(0xFF848688)

// COLOR TEXT IN FIELD
val colorTextInField = Color(0XFF848688)

// COLOR BACKGROUND
val backgroundDark = Color(0xFF484848)
val backgroundLight = Color(0xFFE5E5E5)

// COLOR FOLD
val backgroundFoldDark = Color(0xFF6C6C6C)
val backgroundFoldLight = Color(0xFFD9D9D9)

// COLOR CONTAINER DATA
val containerDataDark = Color(0xFF6C6C6C)
val containerDataLight = Color(0xFFFFFFFF)

private val DarkColorScheme = darkColorScheme(
    primary = colorTextDark, // texto
    background = backgroundDark, // fondo
    primaryContainer = backgroundFoldDark, // pliegue
    secondary = colorTextButtonDark, // textoboton
    onSecondary = colorButtonDark, // boton
    onError = colorTextButtonDisableDark, // textobotondisable
    onErrorContainer = colorButtonDisableDark, // botondisable
    tertiary = colorIconDark, // icono
    onTertiary = colorTextInField, // text-field
    onTertiaryContainer = colorTextInField, // cursor-field
    surfaceContainer = containerDataDark
)

private val LightColorScheme = lightColorScheme(
    primary = colorTextLight, // texto
    background = backgroundLight, // fondo
    primaryContainer = backgroundFoldLight, // pliegue
    secondary = colorTextButtonDark, // textoboton
    onSecondary = colorButtonLight, // boton
    onError = colorTextButtonDisableLight, // textobotondisable
    onErrorContainer = colorButtonDisableLight, // botondisable
    tertiary = colorIconLight, // icono
    onTertiary = colorTextInField, // text-field
    onTertiaryContainer = colorTextInField,
    surfaceContainer = containerDataLight

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
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}