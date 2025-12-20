package com.example.accountingapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- Bouncy Animation Spec ---
// "Q-bounce" feeling: MediumBouncy + Low Stiffness
val BouncySpringSpec = spring<Float>(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessLow
)

// Also for Offset/Size/etc (generic)
fun <T> bouncySpring() = spring<T>(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessLow
)

private val DarkColorScheme = darkColorScheme(
    primary = FreshAirBlue,
    onPrimary = OuterSpace,
    primaryContainer = LilacAccent,
    onPrimaryContainer = PureWhite,
    secondary = ApricotOrange,
    onSecondary = OuterSpace,
    tertiary = SoftGreen,
    background = DarkSlate,
    surface = OuterSpace,
    onBackground = LightText,
    onSurface = LightText
)

private val LightColorScheme = lightColorScheme(
    primary = FreshAirBlue,
    onPrimary = PureWhite,
    primaryContainer = FreshAirBlue.copy(alpha = 0.2f),
    onPrimaryContainer = InkBlack,
    secondary = ApricotOrange,
    onSecondary = InkBlack,
    tertiary = LilacAccent,
    background = WarmPaper,
    surface = PureWhite,
    onBackground = InkBlack,
    onSurface = InkBlack,
    surfaceVariant = LightGray,
    onSurfaceVariant = InkBlack
)

@Composable
fun AccountingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable dynamic color to stick to our Cute theme
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
            window.statusBarColor = colorScheme.background.toArgb() // Match background for immersive feel
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
