package com.aether.calculator.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/**
 * The neutral ramp of a palette: every background shade and every text colour that is not
 * part of an accent.
 */
data class SurfaceTokens(
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val surfaceDim: Color,
    val surfaceBright: Color,
    val surfaceVariant: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val outline: Color,
    val outlineVariant: Color,
    val surfaceTint: Color,
)

/**
 * One accent family: the colour itself, the container it fills, and the light-context
 * ("fixed") variants that do not change with the theme.
 */
data class AccentTokens(
    val base: Color,
    val onBase: Color,
    val container: Color,
    val onContainer: Color,
    val inverse: Color,
    val fixed: Color,
    val fixedDim: Color,
    val onFixed: Color,
    val onFixedVariant: Color,
)

/** The error family. Errors keep the same meaning in every theme, so it never changes. */
data class ErrorTokens(
    val base: Color,
    val onBase: Color,
    val container: Color,
    val onContainer: Color,
)

/**
 * A complete colour scheme for the app.
 *
 * The calculator never reads a raw colour: every surface and every accent comes from one of
 * these palettes, which is what makes the whole app repaint when the theme changes.
 */
data class AetherPalette(
    val key: String,
    val label: String,
    val tagline: String,
    val surfaces: SurfaceTokens,
    val primary: AccentTokens,
    val secondary: AccentTokens,
    val tertiary: AccentTokens,
    val error: ErrorTokens,
)

/**
 * The palettes the app ships with.
 *
 * [Midnight] is the violet the design was drawn in and stays the default; the others keep
 * the same structure - a dark neutral ramp with one dominant accent - so the layout reads
 * the same way whatever the user picks.
 */
object AetherThemes {

    val Midnight = AetherPalette(
        key = "midnight",
        label = "Midnight",
        tagline = "The original violet",
        surfaces = SurfaceTokens(
            background = Color(0xFF111319),
            onBackground = Color(0xFFE2E2EB),
            surface = Color(0xFF111319),
            surfaceDim = Color(0xFF111319),
            surfaceBright = Color(0xFF373940),
            surfaceVariant = Color(0xFF33343B),
            surfaceContainerLowest = Color(0xFF0C0E14),
            surfaceContainerLow = Color(0xFF191B22),
            surfaceContainer = Color(0xFF1E1F26),
            surfaceContainerHigh = Color(0xFF282A30),
            surfaceContainerHighest = Color(0xFF33343B),
            onSurface = Color(0xFFE2E2EB),
            onSurfaceVariant = Color(0xFFC7C4D8),
            inverseSurface = Color(0xFFE2E2EB),
            inverseOnSurface = Color(0xFF2E3037),
            outline = Color(0xFF918FA1),
            outlineVariant = Color(0xFF464555),
            surfaceTint = Color(0xFFC3C0FF),
        ),
        primary = accent(
            base = Color(0xFFC3C0FF),
            onBase = Color(0xFF1D00A5),
            container = Color(0xFF635BFF),
            onContainer = Color(0xFFFEFaff),
            inverse = Color(0xFF4C42E9),
        ),
        secondary = accent(
            base = Color(0xFFFFB3B0),
            onBase = Color(0xFF68000F),
            container = Color(0xFF901822),
            onContainer = Color(0xFFFF9E9B),
            inverse = Color(0xFFBA1C1A),
        ),
        tertiary = accent(
            base = Color(0xFFD0BCFF),
            onBase = Color(0xFF3C0091),
            container = Color(0xFF8354EE),
            onContainer = Color(0xFFFFFAFD),
            inverse = Color(0xFF6B3FD0),
        ),
        error = AetherError,
    )

    val Aurora = AetherPalette(
        key = "aurora",
        label = "Aurora",
        tagline = "Cool teal and mint",
        surfaces = SurfaceTokens(
            background = Color(0xFF0C1214),
            onBackground = Color(0xFFDEE6E4),
            surface = Color(0xFF0C1214),
            surfaceDim = Color(0xFF0C1214),
            surfaceBright = Color(0xFF333C3D),
            surfaceVariant = Color(0xFF323B3C),
            surfaceContainerLowest = Color(0xFF070C0D),
            surfaceContainerLow = Color(0xFF131A1C),
            surfaceContainer = Color(0xFF182022),
            surfaceContainerHigh = Color(0xFF222A2C),
            surfaceContainerHighest = Color(0xFF2D3537),
            onSurface = Color(0xFFDEE6E4),
            onSurfaceVariant = Color(0xFFBEC9C7),
            inverseSurface = Color(0xFFDEE6E4),
            inverseOnSurface = Color(0xFF2D3537),
            outline = Color(0xFF88938F),
            outlineVariant = Color(0xFF3E4947),
            surfaceTint = Color(0xFF7FE7D0),
        ),
        primary = accent(
            base = Color(0xFF7FE7D0),
            onBase = Color(0xFF00382E),
            container = Color(0xFF00A88E),
            onContainer = Color(0xFF00201A),
            inverse = Color(0xFF56DBC0),
        ),
        secondary = accent(
            base = Color(0xFFB4C8FF),
            onBase = Color(0xFF12294F),
            container = Color(0xFF2A4176),
            onContainer = Color(0xFFD5E3FF),
            inverse = Color(0xFF8AA4E8),
        ),
        tertiary = accent(
            base = Color(0xFFCDBFFF),
            onBase = Color(0xFF2A1B5E),
            container = Color(0xFF423282),
            onContainer = Color(0xFFE9DEFF),
            inverse = Color(0xFFA48CE8),
        ),
        error = AetherError,
    )

    val Ember = AetherPalette(
        key = "ember",
        label = "Ember",
        tagline = "Warm amber and coral",
        surfaces = SurfaceTokens(
            background = Color(0xFF14110E),
            onBackground = Color(0xFFEDE4DA),
            surface = Color(0xFF14110E),
            surfaceDim = Color(0xFF14110E),
            surfaceBright = Color(0xFF3A332C),
            surfaceVariant = Color(0xFF342E27),
            surfaceContainerLowest = Color(0xFF0C0A08),
            surfaceContainerLow = Color(0xFF1B1713),
            surfaceContainer = Color(0xFF201C17),
            surfaceContainerHigh = Color(0xFF2A251F),
            surfaceContainerHighest = Color(0xFF342E27),
            onSurface = Color(0xFFEDE4DA),
            onSurfaceVariant = Color(0xFFD1C3B6),
            inverseSurface = Color(0xFFEDE4DA),
            inverseOnSurface = Color(0xFF342E27),
            outline = Color(0xFF9C8E80),
            outlineVariant = Color(0xFF4F453A),
            surfaceTint = Color(0xFFFFB77C),
        ),
        primary = accent(
            base = Color(0xFFFFB77C),
            onBase = Color(0xFF4A2400),
            container = Color(0xFF8F4A00),
            onContainer = Color(0xFFFFDDB8),
            inverse = Color(0xFFE29A5E),
        ),
        secondary = accent(
            base = Color(0xFFFFB4AB),
            onBase = Color(0xFF5C1610),
            container = Color(0xFF7E2A22),
            onContainer = Color(0xFFFFDAD4),
            inverse = Color(0xFFE2948C),
        ),
        tertiary = accent(
            base = Color(0xFFF0DC9A),
            onBase = Color(0xFF403000),
            container = Color(0xFF5A4600),
            onContainer = Color(0xFFFFE7A3),
            inverse = Color(0xFFD6C27F),
        ),
        error = AetherError,
    )

    val Solar = AetherPalette(
        key = "solar",
        label = "Solar",
        tagline = "Golden on graphite",
        surfaces = SurfaceTokens(
            background = Color(0xFF121212),
            onBackground = Color(0xFFE8E6E1),
            surface = Color(0xFF121212),
            surfaceDim = Color(0xFF121212),
            surfaceBright = Color(0xFF383836),
            surfaceVariant = Color(0xFF333331),
            surfaceContainerLowest = Color(0xFF0A0A0A),
            surfaceContainerLow = Color(0xFF1A1A19),
            surfaceContainer = Color(0xFF1F1F1E),
            surfaceContainerHigh = Color(0xFF292927),
            surfaceContainerHighest = Color(0xFF333331),
            onSurface = Color(0xFFE8E6E1),
            onSurfaceVariant = Color(0xFFC9C6BE),
            inverseSurface = Color(0xFFE8E6E1),
            inverseOnSurface = Color(0xFF333331),
            outline = Color(0xFF93908A),
            outlineVariant = Color(0xFF4A4844),
            surfaceTint = Color(0xFFFFD166),
        ),
        primary = accent(
            base = Color(0xFFFFD166),
            onBase = Color(0xFF3D2E00),
            container = Color(0xFF8A6A00),
            onContainer = Color(0xFFFFE9A3),
            inverse = Color(0xFFE0B450),
        ),
        secondary = accent(
            base = Color(0xFFA8D8C0),
            onBase = Color(0xFF0B3B2C),
            container = Color(0xFF1F5544),
            onContainer = Color(0xFFC4F4E0),
            inverse = Color(0xFF87BCA4),
        ),
        tertiary = accent(
            base = Color(0xFFF5BFA0),
            onBase = Color(0xFF4A2100),
            container = Color(0xFF6E3A00),
            onContainer = Color(0xFFFFD9B8),
            inverse = Color(0xFFDB9C7D),
        ),
        error = AetherError,
    )

    val Graphite = AetherPalette(
        key = "graphite",
        label = "Graphite",
        tagline = "Steel blue, almost colourless",
        surfaces = SurfaceTokens(
            background = Color(0xFF111214),
            onBackground = Color(0xFFE3E3E6),
            surface = Color(0xFF111214),
            surfaceDim = Color(0xFF111214),
            surfaceBright = Color(0xFF36373C),
            surfaceVariant = Color(0xFF313236),
            surfaceContainerLowest = Color(0xFF0A0B0C),
            surfaceContainerLow = Color(0xFF171819),
            surfaceContainer = Color(0xFF1C1D1F),
            surfaceContainerHigh = Color(0xFF26272A),
            surfaceContainerHighest = Color(0xFF313236),
            onSurface = Color(0xFFE3E3E6),
            onSurfaceVariant = Color(0xFFC2C3C7),
            inverseSurface = Color(0xFFE3E3E6),
            inverseOnSurface = Color(0xFF313236),
            outline = Color(0xFF8C8D92),
            outlineVariant = Color(0xFF45464B),
            surfaceTint = Color(0xFFBFC7D4),
        ),
        primary = accent(
            base = Color(0xFFBFC7D4),
            onBase = Color(0xFF1E2833),
            container = Color(0xFF3A4553),
            onContainer = Color(0xFFDCE3EE),
            inverse = Color(0xFF97A2B4),
        ),
        secondary = accent(
            base = Color(0xFFC9C9CE),
            onBase = Color(0xFF303034),
            container = Color(0xFF46464C),
            onContainer = Color(0xFFE5E5EA),
            inverse = Color(0xFFA1A1A6),
        ),
        tertiary = accent(
            base = Color(0xFFB7BCC6),
            onBase = Color(0xFF262B33),
            container = Color(0xFF3E444D),
            onContainer = Color(0xFFD3D9E2),
            inverse = Color(0xFF949BA8),
        ),
        error = AetherError,
    )

    /** Every palette, in the order they are offered in the settings sheet. */
    val all: List<AetherPalette> = listOf(Midnight, Aurora, Ember, Solar, Graphite)

    /** The palette the app starts with when nothing has been chosen yet. */
    val default: AetherPalette = Midnight

    /** Looks a palette up by its key, falling back to the default for anything unknown. */
    fun byKey(key: String?): AetherPalette = all.firstOrNull { it.key == key } ?: default
}

/** The error family, shared by every palette. */
private val AetherError = ErrorTokens(
    base = Color(0xFFFFB4AB),
    onBase = Color(0xFF690005),
    container = Color(0xFF93000A),
    onContainer = Color(0xFFFFDAD6),
)

/**
 * Builds an accent family.
 *
 * The "fixed" roles belong to a light context and never change with the theme, so they are
 * derived from the accent instead of being listed for every palette.
 */
private fun accent(
    base: Color,
    onBase: Color,
    container: Color,
    onContainer: Color,
    inverse: Color,
): AccentTokens = AccentTokens(
    base = base,
    onBase = onBase,
    container = container,
    onContainer = onContainer,
    inverse = inverse,
    fixed = lerp(base, Color.White, 0.58f),
    fixedDim = base,
    onFixed = lerp(base, Color.Black, 0.88f),
    onFixedVariant = lerp(container, Color.Black, 0.5f),
)

/**
 * Blends two palettes together.
 *
 * The app cross-fades between themes, so every colour of the palette has to be
 * interpolatable: at [fraction] zero the result is this palette, at one it is [other].
 */
fun AetherPalette.blend(other: AetherPalette, fraction: Float): AetherPalette = AetherPalette(
    key = key,
    label = label,
    tagline = tagline,
    surfaces = surfaces.blend(other.surfaces, fraction),
    primary = primary.blend(other.primary, fraction),
    secondary = secondary.blend(other.secondary, fraction),
    tertiary = tertiary.blend(other.tertiary, fraction),
    error = error.blend(other.error, fraction),
)

private fun SurfaceTokens.blend(other: SurfaceTokens, t: Float) = SurfaceTokens(
    background = lerp(background, other.background, t),
    onBackground = lerp(onBackground, other.onBackground, t),
    surface = lerp(surface, other.surface, t),
    surfaceDim = lerp(surfaceDim, other.surfaceDim, t),
    surfaceBright = lerp(surfaceBright, other.surfaceBright, t),
    surfaceVariant = lerp(surfaceVariant, other.surfaceVariant, t),
    surfaceContainerLowest = lerp(surfaceContainerLowest, other.surfaceContainerLowest, t),
    surfaceContainerLow = lerp(surfaceContainerLow, other.surfaceContainerLow, t),
    surfaceContainer = lerp(surfaceContainer, other.surfaceContainer, t),
    surfaceContainerHigh = lerp(surfaceContainerHigh, other.surfaceContainerHigh, t),
    surfaceContainerHighest = lerp(surfaceContainerHighest, other.surfaceContainerHighest, t),
    onSurface = lerp(onSurface, other.onSurface, t),
    onSurfaceVariant = lerp(onSurfaceVariant, other.onSurfaceVariant, t),
    inverseSurface = lerp(inverseSurface, other.inverseSurface, t),
    inverseOnSurface = lerp(inverseOnSurface, other.inverseOnSurface, t),
    outline = lerp(outline, other.outline, t),
    outlineVariant = lerp(outlineVariant, other.outlineVariant, t),
    surfaceTint = lerp(surfaceTint, other.surfaceTint, t),
)

private fun AccentTokens.blend(other: AccentTokens, t: Float) = AccentTokens(
    base = lerp(base, other.base, t),
    onBase = lerp(onBase, other.onBase, t),
    container = lerp(container, other.container, t),
    onContainer = lerp(onContainer, other.onContainer, t),
    inverse = lerp(inverse, other.inverse, t),
    fixed = lerp(fixed, other.fixed, t),
    fixedDim = lerp(fixedDim, other.fixedDim, t),
    onFixed = lerp(onFixed, other.onFixed, t),
    onFixedVariant = lerp(onFixedVariant, other.onFixedVariant, t),
)

private fun ErrorTokens.blend(other: ErrorTokens, t: Float) = ErrorTokens(
    base = lerp(base, other.base, t),
    onBase = lerp(onBase, other.onBase, t),
    container = lerp(container, other.container, t),
    onContainer = lerp(onContainer, other.onContainer, t),
)
