package com.harish.floatiq.ui.theme
import androidx.compose.ui.graphics.Color
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)
private val NeonBlueScheme = darkColorScheme(
    primary = Color(0xFF2563EB),
    secondary = Color(0xFF3B82F6),
    tertiary = Color(0xFF60A5FA) ,
    background = Color(0xFF020617),
    surface = Color(0xFF0F172A),

    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val EmeraldGreenScheme = darkColorScheme(
    primary = Color(0xFF059669),
    secondary = Color(0xFF10B981),
    tertiary = Color(0xFF34D399),

    background = Color(0xFF02140D),
    surface = Color(0xFF052E1B),

    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val SunsetOrangeScheme = darkColorScheme(
    primary = Color(0xFFEA580C),
    secondary = Color(0xFFF97316),
    tertiary = Color(0xFFFB923C),

    background = Color(0xFF1A0A02),
    surface = Color(0xFF2C1203),

    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val MidnightPurpleScheme = darkColorScheme(
    primary = Color(0xFF7C3AED),
    secondary = Color(0xFF8B5CF6),
    tertiary = Color(0xFFA78BFA),

    background = Color(0xFF0E061C),
    surface = Color(0xFF1A1030),

    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val RCBChampionsScheme = darkColorScheme(

    primary = Color(0xFFEC1C24),
    secondary = Color(0xFFB71C1C),
    tertiary = Color(0xFFFFD700),

    background = Color(0xFF0A0A0A),
    surface = Color(0xFF1A1A1A),

    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)
//private val OnePieceScheme = darkColorScheme(
//
//    primary = Color(0xFFD32F2F),
//    secondary = Color(0xFFFBC02D),
//    tertiary = Color(0xFFFFFFFF),
//
//    background = Color(0xFF0B1F3A),
//    surface = Color(0xFF13294B),
//
//    onPrimary = Color.White,
//    onBackground = Color.White,
//    onSurface = Color.White
//)
@Composable

fun FloatIQTheme(
    selectedTheme: ThemeType,
    content: @Composable () -> Unit
){

    val colorScheme = when (selectedTheme) {

        ThemeType.NEON_BLUE ->
            NeonBlueScheme

        ThemeType.EMERALD_GREEN ->
            EmeraldGreenScheme

        ThemeType.SUNSET_ORANGE ->
            SunsetOrangeScheme

        ThemeType.MIDNIGHT_PURPLE ->
            MidnightPurpleScheme

        ThemeType.RCB_CHAMPIONS ->
            RCBChampionsScheme

//        ThemeType.ONE_PIECE ->
//            OnePieceScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}