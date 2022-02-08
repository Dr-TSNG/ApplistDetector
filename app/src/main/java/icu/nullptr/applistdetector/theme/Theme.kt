package icu.nullptr.applistdetector.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF5DD4FC),
    onPrimary = Color(0xFF003544),
    primaryContainer = Color(0xFF004D62),
    onPrimaryContainer = Color(0xFFB5EAFF),

    secondary = Color(0xFFB3CAD5),
    onSecondary = Color(0xFF1E333B),
    secondaryContainer = Color(0xFF354A53),
    onSecondaryContainer = Color(0xFFCFE6F1),

    tertiary = Color(0xFFC4C3EA),
    onTertiary = Color(0xFF2C2D4D),
    tertiaryContainer = Color(0xFF434465),
    onTertiaryContainer = Color(0xFFE1E0FF),

    error = Color(0xFFFFB4A9),
    onError = Color(0xFF680003),
    errorContainer = Color(0xFF930006),
    onErrorContainer = Color(0xFFFFDAD4),

    background = Color(0xFF191C1D),
    onBackground = Color(0xFFE1E3E5),
    surface = Color(0xFF191C1D),
    onSurface = Color(0xFFE1E3E5),
    surfaceVariant = Color(0xFF40484C),
    onSurfaceVariant = Color(0xFFC0C8CC),
    outline = Color(0xFF8A9296)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006782),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB5EAFF),
    onPrimaryContainer = Color(0xFF001F29),

    secondary = Color(0xFF4C626B),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCFE6F1),
    onSecondaryContainer = Color(0xFF071E26),

    tertiary = Color(0xFF5A5B7D),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE1E0FF),
    onTertiaryContainer = Color(0xFF171837),

    error = Color(0xFFBA1B1B),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF410001),

    background = Color(0xFFF9FDFF),
    onBackground = Color(0xFF191C1D),
    surface = Color(0xFFF9FDFF),
    onSurface = Color(0xFF191C1D),
    surfaceVariant = Color(0xFFF9FDFF),
    onSurfaceVariant = Color(0xFF40484C),
    outline = Color(0xFF70787C)
)

@Composable
fun MyTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    enableDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        enableDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
