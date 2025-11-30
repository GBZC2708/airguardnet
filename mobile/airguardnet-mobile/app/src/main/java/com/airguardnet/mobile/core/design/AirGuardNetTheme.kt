package com.airguardnet.mobile.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFF38E8FF),
    onPrimary = Color(0xFF00141B),
    background = Color(0xFF0B1A24),
    surface = Color(0xFF102635),
    surfaceVariant = Color(0xFF173445),
    onSurface = Color(0xFFE5F7FF),
    secondary = Color(0xFF40C057)
)

@Composable
fun AirGuardNetTheme(content: @Composable () -> Unit) {
    val colors = DarkColors
    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content
    )
}
