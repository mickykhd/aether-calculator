package com.aether.calculator.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Corner radii of the design system.
 *
 * The design language is "pill shaped": keypad keys are complete stadiums, and so are
 * the segmented control, the chips and the copy button.
 */
object AetherShapes {
    val Sm = RoundedCornerShape(8.dp)
    val Default = RoundedCornerShape(16.dp)
    val Md = RoundedCornerShape(24.dp)
    val Lg = RoundedCornerShape(32.dp)
    val Xl = RoundedCornerShape(48.dp)
    val Full = RoundedCornerShape(9999.dp)

    /** Keypad key: full pill geometry, exactly as the reference screen shows. */
    val Key = RoundedCornerShape(50)
}

val AetherMaterialShapes = Shapes(
    extraSmall = AetherShapes.Sm,
    small = AetherShapes.Default,
    medium = AetherShapes.Md,
    large = AetherShapes.Lg,
    extraLarge = AetherShapes.Xl,
)
