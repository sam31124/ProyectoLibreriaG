package com.libreriag.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = YellowPrimary,
    onPrimary = BlueDark,

    secondary = GreenHat,
    onSecondary = WhiteBG,

    tertiary = BrownHair,
    onTertiary = WhiteBG,

    background = WhiteBG,
    onBackground = BlueDark,

    surface = WhiteBG,
    onSurface = BlueDark
)

private val DarkColors = darkColorScheme(
    primary = YellowPrimary,
    onPrimary = BlueDark,

    secondary = GreenHatDark,
    onSecondary = WhiteBG,

    tertiary = BrownHair,
    onTertiary = WhiteBG,

    background = BlueDark,
    onBackground = WhiteBG,

    surface = BlueDark,
    onSurface = WhiteBG
)

@Composable
fun LibreriaGTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivado para respetar tus colores del logo
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
