package com.dorianbanic.core.desingsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun HeyAppTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalExtendedColors provides LightExtendedColors) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = Typography,
            content = content
        )
    }
}