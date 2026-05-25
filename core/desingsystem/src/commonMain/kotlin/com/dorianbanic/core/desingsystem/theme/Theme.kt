package com.dorianbanic.core.desingsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val ColorScheme.extended: ExtendedColors
    @ReadOnlyComposable
    @Composable
    get() = LocalExtendedColors.current

@Immutable
data class ExtendedColors(
    // Button states
    val primaryHover: Color,
    val destructiveHover: Color,
    val destructiveSecondaryOutline: Color,
    val disabledOutline: Color,
    val disabledFill: Color,
    val successOutline: Color,
    val success: Color,
    val onSuccess: Color,
    val secondaryFill: Color,

    // Text variants
    val textPrimary: Color,
    val textTertiary: Color,
    val textSecondary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,

    // Surface variants
    val surfaceLower: Color,
    val surfaceHigher: Color,
    val surfaceOutline: Color,
    val overlay: Color,

    // Accent colors
    val accentBlue: Color,
    val accentPurple: Color,
    val accentViolet: Color,
    val accentPink: Color,
    val accentOrange: Color,
    val accentYellow: Color,
    val accentGreen: Color,
    val accentTeal: Color,
    val accentLightBlue: Color,
    val accentGrey: Color,

    // Cake colors for chat bubbles
    val cakeViolet: Color,
    val cakeGreen: Color,
    val cakeBlue: Color,
    val cakePink: Color,
    val cakeOrange: Color,
    val cakeYellow: Color,
    val cakeTeal: Color,
    val cakePurple: Color,
    val cakeRed: Color,
    val cakeMint: Color,
)

val LightExtendedColors = ExtendedColors(
    primaryHover = HeyappBrand600,
    destructiveHover = HeyappRed600,
    destructiveSecondaryOutline = HeyappRed200,
    disabledOutline = HeyappBase200,
    disabledFill = HeyappBase150,
    successOutline = HeyappBrand100,
    success = HeyappBrand600,
    onSuccess = HeyappBase0,
    secondaryFill = HeyappBase100,

    textPrimary = HeyappBase1000,
    textTertiary = HeyappBase800,
    textSecondary = HeyappBase900,
    textPlaceholder = HeyappBase700,
    textDisabled = HeyappBase400,

    surfaceLower = HeyappBase100,
    surfaceHigher = HeyappBase100,
    surfaceOutline = HeyappBase1000Alpha14,
    overlay = HeyappBase1000Alpha80,

    accentBlue = HeyappBlue,
    accentPurple = HeyappPurple,
    accentViolet = HeyappViolet,
    accentPink = HeyappPink,
    accentOrange = HeyappOrange,
    accentYellow = HeyappYellow,
    accentGreen = HeyappGreen,
    accentTeal = HeyappTeal,
    accentLightBlue = HeyappLightBlue,
    accentGrey = HeyappGrey,

    cakeViolet = HeyappCakeLightViolet,
    cakeGreen = HeyappCakeLightGreen,
    cakeBlue = HeyappCakeLightBlue,
    cakePink = HeyappCakeLightPink,
    cakeOrange = HeyappCakeLightOrange,
    cakeYellow = HeyappCakeLightYellow,
    cakeTeal = HeyappCakeLightTeal,
    cakePurple = HeyappCakeLightPurple,
    cakeRed = HeyappCakeLightRed,
    cakeMint = HeyappCakeLightMint,
)

val LightColorScheme = lightColorScheme(
    primary = HeyappBrand500,
    onPrimary = HeyappBrand1000,
    primaryContainer = HeyappBrand100,
    onPrimaryContainer = HeyappBrand900,

    secondary = HeyappBase700,
    onSecondary = HeyappBase0,
    secondaryContainer = HeyappBase100,
    onSecondaryContainer = HeyappBase900,

    tertiary = HeyappBrand900,
    onTertiary = HeyappBase0,
    tertiaryContainer = HeyappBrand100,
    onTertiaryContainer = HeyappBrand1000,

    error = HeyappRed500,
    onError = HeyappBase0,
    errorContainer = HeyappRed200,
    onErrorContainer = HeyappRed600,

    background = HeyappBrand1000,
    onBackground = HeyappBase0,
    surface = HeyappBase0,
    onSurface = HeyappBase1000,
    surfaceVariant = HeyappBase100,
    onSurfaceVariant = HeyappBase900,

    outline = HeyappBase1000Alpha80,
    outlineVariant = HeyappBase200,
)