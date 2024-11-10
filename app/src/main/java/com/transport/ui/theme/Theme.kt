package com.transport.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.transport.R

private val DarkColorScheme = darkColorScheme(
    primary = Red,
    secondary = Peach,
    tertiary = LightGrey,
    onTertiary = Grey
)

private val LightColorScheme = lightColorScheme(
    primary = Red,
    secondary = Peach,
    tertiary = LightGrey,
    onTertiary = Grey

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

val LocalExtendedFonts = staticCompositionLocalOf { ExtendedFonts() }

val extendedFonts = ExtendedFonts(
    rubikFontFamily = FontFamily(
        Font(R.font.rubik_light, FontWeight.Light),
        Font(R.font.rubik_regular, FontWeight.W400),
        Font(R.font.rubik_medium, FontWeight.Medium),
        Font(R.font.rubik_semi_bold, FontWeight.SemiBold),
        Font(R.font.rubik_bold, FontWeight.Bold),
        Font(R.font.rubik_black, FontWeight.Black)
    )
)

@Composable
fun TransportTheme(
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

    CompositionLocalProvider(
        LocalExtendedFonts provides extendedFonts
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object ExtendedTheme {
    val extendedFonts: ExtendedFonts
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedFonts.current
}