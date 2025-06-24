package com.example.ecommerceapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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