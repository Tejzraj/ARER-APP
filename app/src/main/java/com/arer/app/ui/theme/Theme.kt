package com.arer.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ArerPrimary,
    onPrimary = ArerOnPrimary,
    primaryContainer = ArerPrimaryContainer,
    onPrimaryContainer = ArerOnPrimaryContainer,
    secondary = ArerSecondary,
    onSecondary = ArerOnSecondary,
    secondaryContainer = ArerSecondaryContainer,
    onSecondaryContainer = ArerOnSecondaryContainer,
    background = ArerBackground,
    onBackground = ArerOnBackground,
    surface = ArerSurface,
    onSurface = ArerOnSurface
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFB923C),
    onPrimary = Color(0xFF431407),
    primaryContainer = Color(0xFFC2410C),
    onPrimaryContainer = Color(0xFFFFEDD5),
    secondary = Color(0xFFF97316),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF9A3412),
    onSecondaryContainer = Color(0xFFFFDBCC),
    background = ArerDarkBackground,
    onBackground = ArerDarkOnSurface,
    surface = ArerDarkSurface,
    onSurface = ArerDarkOnSurface
)

@Composable
fun ARERAPPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled to preserve ARER Orange brand identity
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
