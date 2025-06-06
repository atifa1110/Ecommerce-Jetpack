package com.example.ecommerceapp.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = DarkPurple,
    onPrimary = Black,
    background = DarkGray,
    onBackground = White,
    primaryContainer = primaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    secondaryContainer = secondaryContainerDark,
    errorContainer = errorDark,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple,
    onPrimary = White,
    background = White,
    onBackground = DarkGray,
    primaryContainer = primaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    secondaryContainer = secondaryContainer,
    errorContainer = errorDark,

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
fun EcommerceAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}