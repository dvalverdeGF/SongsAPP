package com.grafitto.songsapp.ui.theme

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

private val AppPrimaryColor = Color(0xFF1E6866)
private val AppOnPrimaryColor = Color.White

private val DarkColorScheme =
    darkColorScheme(
        primary = AppPrimaryColor,
        onPrimary = AppOnPrimaryColor,
        secondary = PurpleGrey80,
        tertiary = Pink80,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = AppPrimaryColor,
        onPrimary = AppOnPrimaryColor,
        secondary = PurpleGrey40,
        tertiary = Pink40,
        background = Color.White,
        surface = Color.White,
    )

@Suppress("ktlint:standard:function-naming")
@Composable
fun SongsAPPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colorScheme =
        MaterialTheme.colorScheme.copy(
            primary = AppPrimaryColor,
            onPrimary = AppOnPrimaryColor,
            primaryContainer = AppPrimaryColor,
            onPrimaryContainer = AppOnPrimaryColor,
        )
    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content,
    )
}
