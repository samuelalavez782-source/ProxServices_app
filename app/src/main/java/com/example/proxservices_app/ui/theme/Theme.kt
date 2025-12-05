package com.example.proxservices_app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 1. ESTRUCTURA PARA LOS COLORES QUE NO TIENEN UN ROL ESTÁNDAR EN MATERIAL THEME
// ------------------------------------------------------------------------------------
@Immutable
data class ExtendedColors(
    val colorStateYellow: Color,
    val colorStateBlue: Color,
    val textSecondary: Color,
    val starColor: Color,
    val disabled: Color,
    val specialBackground: Color,
)

private val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        colorStateYellow = Color.Unspecified,
        colorStateBlue = Color.Unspecified,
        textSecondary = Color.Unspecified,
        starColor = Color.Unspecified,
        disabled = Color.Unspecified,
        specialBackground = Color.Unspecified
    )
}

// 2. ASIGNACIÓN DE COLORES DE TU Color.kt A LOS ROLES DEL TEMA
// ------------------------------------------------------------------

// Asignación de colores extendidos para el tema claro
private val extendedLightColors = ExtendedColors(
    colorStateYellow = AmarilloNotificacion,
    colorStateBlue = AzulVerificado,
    textSecondary = GrisTextoSecundario,
    starColor = DoradoEstrellas,
    disabled = AzulDesactivado,
    specialBackground = AzulClaroFondo
)

// Asignación de colores extendidos para el tema oscuro (puedes ajustarlos si es necesario)
private val extendedDarkColors = ExtendedColors(
    colorStateYellow = AmarilloNotificacion,
    colorStateBlue = AzulVerificado,
    textSecondary = GrisTextoSecundario,
    starColor = DoradoEstrellas,
    disabled = AzulDesactivado,
    specialBackground = AzulProfundoTexto // Se usa un color oscuro para contraste
)

// Paleta estándar de Material Design para el tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = PrincipalAzul,
    secondary = AzulProfundoTexto,
    background = Negro,
    surface = GrisTextoPrincipal,
    onPrimary = Blanco,
    onSecondary = Blanco,
    onBackground = Blanco,
    onSurface = Blanco
)

// Paleta estándar de Material Design para el tema claro
private val LightColorScheme = lightColorScheme(
    primary = PrincipalAzul,
    secondary = AzulProfundoTexto,
    tertiary = AzulHipervinculo,
    background = GrisFondo,
    surface = Blanco,
    onPrimary = Blanco,
    onSecondary = Blanco,
    onTertiary = Blanco,
    onBackground = GrisTextoPrincipal,
    onSurface = GrisTextoPrincipal,
    outline = GrisBordeInput
)

// 3. OBJETO DE ACCESO Y COMPOSABLE DEL TEMA
// ---------------------------------------------------

/**
 * Objeto para acceder fácilmente a los colores extendidos en la UI.
 * Uso: AppTheme.extendedColors.specialBackground
 */
object AppTheme {
    val extendedColors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}

@Composable
fun ProxServices_appTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = if (darkTheme) extendedDarkColors else extendedLightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography, // Asume que tienes un archivo Typography.kt
            content = content
        )
    }
}//fin






