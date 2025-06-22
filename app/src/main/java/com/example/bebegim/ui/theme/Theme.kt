package com.example.bebegim.ui.theme

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

// Eksik renkler
private val PureWhite = Color(0xFFFFFFFF)
private val DarkNavy = Color(0xFF0D47A1)

private val LightColorScheme = lightColorScheme(
    primary = SafeBlue,
    onPrimary = PureWhite,
    primaryContainer = LightBlue,
    onPrimaryContainer = DarkBlue,

    secondary = WarmOrange,
    onSecondary = PureWhite,
    secondaryContainer = LightOrange,
    onSecondaryContainer = DarkOrange,

    tertiary = SoftMint,
    onTertiary = PureWhite,
    tertiaryContainer = VeryLightMint,
    onTertiaryContainer = DarkMint,

    error = AlertRed,
    onError = PureWhite,
    errorContainer = LightRed,
    onErrorContainer = DarkRed,

    background = PastelBlueWhite,
    onBackground = DeepGray,

    surface = SoftPastelBlue,
    onSurface = DeepGray,
    surfaceVariant = VeryLightGray,
    onSurfaceVariant = MediumGray,

    outline = LightGray,
    outlineVariant = VeryLightGray,

    scrim = Color.Black.copy(alpha = 0.5f),

    inverseSurface = DeepGray,
    inverseOnSurface = PureWhite,
    inversePrimary = LightSafeBlue,
)

private val DarkColorScheme = darkColorScheme(
    primary = LightSafeBlue,
    onPrimary = DarkNavy,
    primaryContainer = DarkBlueContainer,
    onPrimaryContainer = LightBlue,

    secondary = SoftWarmOrange,
    onSecondary = DarkNavy,
    secondaryContainer = DarkOrangeContainer,
    onSecondaryContainer = LightOrange,

    tertiary = DarkSoftMint,
    onTertiary = DarkNavy,
    tertiaryContainer = DarkMintContainer,
    onTertiaryContainer = VeryLightMint,

    error = LightAlertRed,
    onError = DarkNavy,
    errorContainer = DarkRedContainer,
    onErrorContainer = LightRed,

    background = DarkPastelBlue,
    onBackground = VeryLightGray,

    surface = DarkPastelBlueSurface,
    onSurface = VeryLightGray,
    surfaceVariant = DarkGraySurface,
    onSurfaceVariant = LightGray,

    outline = MediumGray,
    outlineVariant = DarkGray,

    scrim = Color.Black.copy(alpha = 0.7f),

    inverseSurface = VeryLightGray,
    inverseOnSurface = DeepGray,
    inversePrimary = SafeBlue,
)

@Composable
fun BabyProject(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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
        shapes = Shapes,
        content = content
    )
}