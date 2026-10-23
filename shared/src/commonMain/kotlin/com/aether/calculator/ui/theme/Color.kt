package com.aether.calculator.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/**
 * Every colour the app draws with.
 *
 * The names are the design tokens of the calculator and they never change. What changes is
 * the palette behind them: the active palette lives in a snapshot state, so a read of
 * `AetherColors.Primary` inside a composable subscribes to it. When the theme changes, the
 * whole screen is redrawn in the new colours without a single call site changing.
 */
object AetherColors {

    /** The palette that is on screen right now. */
    private var live by mutableStateOf(AetherThemes.default)

    /** The palette the app is heading towards. */
    private var target by mutableStateOf(AetherThemes.default)

    /** The palette that is on screen right now. */
    val current: AetherPalette get() = live

    /** The palette the app is heading towards. */
    val targetPalette: AetherPalette get() = target

    /**
     * Puts a palette on screen straight away, without a transition.
     *
     * This is what the app uses at start-up, where the stored theme has to be in place
     * before the first frame is drawn.
     */
    fun apply(palette: AetherPalette) {
        live = palette
        target = palette
    }

    /**
     * Asks for a palette. The theme cross-fades towards it.
     */
    fun request(palette: AetherPalette) {
        target = palette
    }

    /**
     * Moves the on-screen palette one step towards the target.
     *
     * Called by the theme while it blends the two palettes together.
     */
    internal fun blendTo(from: AetherPalette, to: AetherPalette, fraction: Float) {
        live = from.blend(to, fraction)
    }

    // Surfaces

    val Surface: Color get() = current.surfaces.surface
    val SurfaceDim: Color get() = current.surfaces.surfaceDim
    val SurfaceBright: Color get() = current.surfaces.surfaceBright
    val SurfaceContainerLowest: Color get() = current.surfaces.surfaceContainerLowest
    val SurfaceContainerLow: Color get() = current.surfaces.surfaceContainerLow
    val SurfaceContainer: Color get() = current.surfaces.surfaceContainer
    val SurfaceContainerHigh: Color get() = current.surfaces.surfaceContainerHigh
    val SurfaceContainerHighest: Color get() = current.surfaces.surfaceContainerHighest
    val OnSurface: Color get() = current.surfaces.onSurface
    val OnSurfaceVariant: Color get() = current.surfaces.onSurfaceVariant
    val InverseSurface: Color get() = current.surfaces.inverseSurface
    val InverseOnSurface: Color get() = current.surfaces.inverseOnSurface
    val Outline: Color get() = current.surfaces.outline
    val OutlineVariant: Color get() = current.surfaces.outlineVariant
    val SurfaceTint: Color get() = current.surfaces.surfaceTint
    val Background: Color get() = current.surfaces.background
    val OnBackground: Color get() = current.surfaces.onBackground
    val SurfaceVariant: Color get() = current.surfaces.surfaceVariant

    // Primary

    val Primary: Color get() = current.primary.base
    val OnPrimary: Color get() = current.primary.onBase
    val PrimaryContainer: Color get() = current.primary.container
    val OnPrimaryContainer: Color get() = current.primary.onContainer
    val InversePrimary: Color get() = current.primary.inverse
    val PrimaryFixed: Color get() = current.primary.fixed
    val PrimaryFixedDim: Color get() = current.primary.fixedDim
    val OnPrimaryFixed: Color get() = current.primary.onFixed
    val OnPrimaryFixedVariant: Color get() = current.primary.onFixedVariant

    // Secondary

    val Secondary: Color get() = current.secondary.base
    val OnSecondary: Color get() = current.secondary.onBase
    val SecondaryContainer: Color get() = current.secondary.container
    val OnSecondaryContainer: Color get() = current.secondary.onContainer
    val SecondaryFixed: Color get() = current.secondary.fixed
    val SecondaryFixedDim: Color get() = current.secondary.fixedDim
    val OnSecondaryFixed: Color get() = current.secondary.onFixed
    val OnSecondaryFixedVariant: Color get() = current.secondary.onFixedVariant

    // Tertiary

    val Tertiary: Color get() = current.tertiary.base
    val OnTertiary: Color get() = current.tertiary.onBase
    val TertiaryContainer: Color get() = current.tertiary.container
    val OnTertiaryContainer: Color get() = current.tertiary.onContainer
    val TertiaryFixed: Color get() = current.tertiary.fixed
    val TertiaryFixedDim: Color get() = current.tertiary.fixedDim
    val OnTertiaryFixed: Color get() = current.tertiary.onFixed
    val OnTertiaryFixedVariant: Color get() = current.tertiary.onFixedVariant

    // Error

    val Error: Color get() = current.error.base
    val OnError: Color get() = current.error.onBase
    val ErrorContainer: Color get() = current.error.container
    val OnErrorContainer: Color get() = current.error.onContainer
}
