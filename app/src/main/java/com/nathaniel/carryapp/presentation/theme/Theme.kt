package com.nathaniel.carryapp.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*

import com.nathaniel.carryapp.presentation.utils.*

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

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

data class AppColors(
    val primary: Color = Color(0xFF4CAF50),
    val primaryDark: Color = Color(0xFF2E7D32),
    val textPrimary: Color = Color.White,
    val textSecondary: Color = Color.LightGray,
    val error: Color = Color.Red
)

val LocalAppColors = staticCompositionLocalOf { AppColors() }

data class AppSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val lsm: Dp = 12.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp
)

val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }

data class AppTypography(
    val titleLarge: TextStyle = TextStyle(fontSize = 22.sp),
    val body: TextStyle = TextStyle(fontSize = 16.sp),
    val caption: TextStyle = TextStyle(fontSize = 12.sp)
)

val LocalAppTypography = staticCompositionLocalOf { AppTypography() }

data class ResponsiveSizes(
    val logoSize: Dp,
    val buttonHeight: Dp,
    val buttonWidth: Dp,
    val titleFontSize: androidx.compose.ui.unit.TextUnit,
    val buttonFontSize: androidx.compose.ui.unit.TextUnit,
    val paddingHorizontal: Dp,
    val paddingVertical: Dp,
    val iconSize: Dp,
    val labelFontSize: androidx.compose.ui.unit.TextUnit,
)

val LocalResponsiveSizes = staticCompositionLocalOf {
    ResponsiveSizes(
        logoSize = 200.dp,
        buttonHeight = 64.dp,
        buttonWidth = 170.dp,
        titleFontSize = 24.sp,
        buttonFontSize = 16.sp,
        paddingHorizontal = 24.dp,
        paddingVertical = 32.dp,
        iconSize = 20.dp,
        labelFontSize = 16.sp
    )
}

@Composable
fun CarryappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    val responsiveSizes = ResponsiveSizes(
        logoSize = responsiveHeightFraction(0.4f, min = 160.dp, max = 320.dp),
        buttonHeight = responsiveHeightFraction(0.07f, min = 50.dp, max = 76.dp),
        buttonWidth = responsiveWidthFraction(0.45f, min = 140.dp, max = 240.dp),
        titleFontSize = responsiveTextSp(base = 18f, scaleFactor = 0.03f, min = 14f, max = 28f),
        buttonFontSize = responsiveTextSp(base = 14f, scaleFactor = 0.025f, min = 12f, max = 22f),
        paddingHorizontal = responsiveWidthFraction(0.06f, min = 16.dp, max = 32.dp),
        paddingVertical = responsiveHeightFraction(0.05f, min = 16.dp, max = 40.dp),
        iconSize = responsiveWidthFraction(0.07f, min = 18.dp, max = 32.dp),
        labelFontSize = responsiveTextSp(base = 18f, scaleFactor = 0.03f, min = 14f, max = 28f),
    )

    CompositionLocalProvider(
        LocalResponsiveSizes provides responsiveSizes,
        LocalAppColors provides AppColors(),
        LocalAppSpacing provides AppSpacing(),
        LocalAppTypography provides AppTypography()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
