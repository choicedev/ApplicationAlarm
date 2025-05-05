package com.choice.wakeuplanner.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NeonYellow,
    background = DarkGray,
    surface = DarkerGray,
    onPrimary = DarkerGray,
    onBackground = PureWhite,
    onSurface = PureWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = NeonYellow,
    background = SoftPurple,
    surface = PureWhite,
    onPrimary = DarkerGray,
    onBackground = DarkGray,
    onSurface = DarkGray,
)
@Composable
fun ApplicationAlarmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            dynamicLightColorScheme(context)
        }
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}