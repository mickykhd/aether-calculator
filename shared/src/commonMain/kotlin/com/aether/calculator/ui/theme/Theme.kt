package com.aether.calculator.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos

/**
 * The Material colour scheme of a palette.
 */
fun AetherPalette.colorScheme(): ColorScheme = darkColorScheme(
    primary = primary.base,
    onPrimary = primary.onBase,
    primaryContainer = primary.container,
    onPrimaryContainer = primary.onContainer,
    inversePrimary = primary.inverse,
    secondary = secondary.base,
    onSecondary = secondary.onBase,
    secondaryContainer = secondary.container,
    onSecondaryContainer = secondary.onContainer,
    tertiary = tertiary.base,
    onTertiary = tertiary.onBase,
    tertiaryContainer = tertiary.container,
    onTertiaryContainer = tertiary.onContainer,
    error = error.base,
    onError = error.onBase,
    errorContainer = error.container,
    onErrorContainer = error.onContainer,
    background = surfaces.background,
    onBackground = surfaces.onBackground,
    surface = surfaces.surface,
    onSurface = surfaces.onSurface,
    surfaceVariant = surfaces.surfaceVariant,
    onSurfaceVariant = surfaces.onSurfaceVariant,
    outline = surfaces.outline,
    outlineVariant = surfaces.outlineVariant,
    inverseSurface = surfaces.inverseSurface,
    inverseOnSurface = surfaces.inverseOnSurface,
    surfaceDim = surfaces.surfaceDim,
    surfaceBright = surfaces.surfaceBright,
    surfaceContainerLowest = surfaces.surfaceContainerLowest,
    surfaceContainerLow = surfaces.surfaceContainerLow,
    surfaceContainer = surfaces.surfaceContainer,
    surfaceContainerHigh = surfaces.surfaceContainerHigh,
    surfaceContainerHighest = surfaces.surfaceContainerHighest,
    surfaceTint = surfaces.surfaceTint,
)

/**
 * Applies the Aether Compute theme to every platform.
 *
 * The theme follows the active palette, so the whole app repaints in the colours of the
 * theme the user picked. Switching themes is a cross-fade: the two palettes are blended
 * into each other over a fraction of a second instead of snapping.
 */
@Composable
fun AetherTheme(content: @Composable () -> Unit) {
    val target = AetherColors.targetPalette

    LaunchedEffect(target) {
        val from = AetherColors.current
        if (from == target) return@LaunchedEffect
        // The two palettes are blended into each other, one frame at a time.
        val durationNanos = 420_000_000L
        var startNanos = 0L
        var elapsed = 0L
        while (elapsed < durationNanos) {
            elapsed = withFrameNanos { now ->
                if (startNanos == 0L) startNanos = now
                now - startNanos
            }
            val raw = (elapsed.toFloat() / durationNanos).coerceIn(0f, 1f)
            // Smoothed, so the colours ease into place instead of running at a constant speed.
            AetherColors.blendTo(from, target, raw * raw * (3f - 2f * raw))
        }
        AetherColors.blendTo(from, target, 1f)
    }

    val palette = AetherColors.current
    val colorScheme = remember(palette) { palette.colorScheme() }
    val fonts = rememberAetherFonts()
    CompositionLocalProvider(LocalAetherFonts provides fonts) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = aetherTypography(),
            shapes = AetherMaterialShapes,
            content = content,
        )
    }
}
