package com.example.seniorproject_icebreaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = Colors.Black,
    secondary = Colors.LightSchemePolarBlue,
    background = Color.White,
    surface = Color.White,
    error = Colors.LightSchemeRed,
    onPrimary = Colors.White,
    onSecondary = Colors.White,
    onBackground = Colors.Black,
    onSurface = Colors.Black,
    onError = Colors.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Colors.White,
    secondary = Colors.DarkSchemePolarBlue,
    background = Color.Black,
    surface = Color.Black,
    error = Colors.DarkSchemeRed,
    onPrimary = Colors.Black,
    onSecondary = Colors.Black,
    onBackground = Colors.White,
    onSurface = Colors.White,
    onError = Colors.Black,
)

object AppPadding {
    val small: Dp = 8.dp
    val medium: Dp = 16.dp
    val large: Dp = 24.dp
}

@Composable
fun IceBreakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}