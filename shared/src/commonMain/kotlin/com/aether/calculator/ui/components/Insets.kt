package com.aether.calculator.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The insets that keep content clear of the status bar and of any camera cutout.
 *
 * Only the top edge and the sides are taken; the bottom is handled separately by the
 * navigation bar and keyboard padding.
 */
@Composable
fun safeTopInsets(): WindowInsets =
    WindowInsets.statusBars
        .union(WindowInsets.displayCutout)
        .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)

/**
 * How far a centred camera cutout hangs below the status bar.
 *
 * Phones such as the iQOO Neo 10R carry a punch hole that is drawn as a floating island
 * inside the status bar. When that island dips below the status bar strip, anything
 * drawn at the top of the screen would collide with it, so the app bar is pushed down by
 * exactly the overhang. Devices without a cutout report zero and keep their normal height.
 */
@Composable
fun rememberIslandOverhang(): Dp {
    val density = LocalDensity.current
    val cutout = WindowInsets.displayCutout.only(WindowInsetsSides.Bottom)
    val bars = WindowInsets.statusBars
    val cutoutBottom = with(density) { cutout.getBottom(this).toDp() }
    val statusBarHeight = with(density) { bars.getTop(this).toDp() }
    return (cutoutBottom - statusBarHeight).coerceAtLeast(0.dp)
}
