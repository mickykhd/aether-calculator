package com.aether.calculator.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.aether.calculator.resources.Res
import com.aether.calculator.resources.inter_regular
import com.aether.calculator.resources.space_grotesk_bold
import com.aether.calculator.resources.space_grotesk_medium
import com.aether.calculator.resources.space_grotesk_regular
import com.aether.calculator.resources.space_mono_bold
import com.aether.calculator.resources.space_mono_regular
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

/**
 * The three families the design system relies on.
 *
 * Compose Multiplatform loads bundled fonts asynchronously, so the families have to be
 * created inside a composition and handed down through [LocalAetherFonts].
 */
@Immutable
class AetherFonts(
    val mono: FontFamily,
    val grotesk: FontFamily,
    val inter: FontFamily,
)

/** Holds the bundled families for the whole application. */
val LocalAetherFonts = staticCompositionLocalOf<AetherFonts> {
    error("AetherFonts were not provided. Wrap the UI in AetherTheme.")
}

@Composable
@OptIn(ExperimentalResourceApi::class)
internal fun rememberAetherFonts(): AetherFonts {
    val monoRegular = Font(Res.font.space_mono_regular, FontWeight.Normal)
    val monoBold = Font(Res.font.space_mono_bold, FontWeight.Bold)
    val groteskRegular = Font(Res.font.space_grotesk_regular, FontWeight.Normal)
    val groteskMedium = Font(Res.font.space_grotesk_medium, FontWeight.Medium)
    val groteskSemibold = Font(Res.font.space_grotesk_bold, FontWeight.SemiBold)
    val groteskBold = Font(Res.font.space_grotesk_bold, FontWeight.Bold)
    val interRegular = Font(Res.font.inter_regular, FontWeight.Normal)

    return AetherFonts(
        mono = remember(monoRegular, monoBold) { FontFamily(monoRegular, monoBold) },
        grotesk = remember(groteskRegular, groteskMedium, groteskSemibold, groteskBold) {
            FontFamily(groteskRegular, groteskMedium, groteskSemibold, groteskBold)
        },
        inter = remember(interRegular) { FontFamily(interRegular) },
    )
}

/**
 * The type scale of the design system.
 *
 * Readouts use a monospaced face so digits never jitter while typing, key caps use a
 * geometric sans and the algebraic operators use a neutral sans for optical symmetry.
 */
object AetherType {

    val HeadlineDisplayMobile: TextStyle
        @Composable get() = TextStyle(
            fontFamily = LocalAetherFonts.current.mono,
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 52.sp,
            letterSpacing = (-0.04).em,
        )

    val HeadlineDisplayCompressed: TextStyle
        @Composable get() = TextStyle(
            fontFamily = LocalAetherFonts.current.mono,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp,
            letterSpacing = (-0.03).em,
        )

    val HeadlineExpression: TextStyle
        @Composable get() = TextStyle(
            fontFamily = LocalAetherFonts.current.mono,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 28.sp,
            letterSpacing = (-0.01).em,
        )

    /**
     * The app bar title.
     *
     * The reference screen sets the wordmark in the geometric sans rather than the
     * monospaced expression face, so it gets its own token.
     */
    val HeadlineAppBarTitle: TextStyle
        @Composable get() = TextStyle(
            fontFamily = LocalAetherFonts.current.grotesk,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp,
            letterSpacing = (-0.01).em,
        )

    val HeadlineKeypadPrimary: TextStyle
        @Composable get() = TextStyle(
            fontFamily = LocalAetherFonts.current.grotesk,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 32.sp,
            letterSpacing = 0.em,
        )

    val HeadlineKeypadOperator: TextStyle
        @Composable get() = TextStyle(
            fontFamily = LocalAetherFonts.current.inter,
            fontSize = 30.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 32.sp,
            letterSpacing = 0.em,
        )

    val BodyMedium: TextStyle
        @Composable get() = TextStyle(
            fontFamily = LocalAetherFonts.current.inter,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 24.sp,
            letterSpacing = 0.em,
        )

    val BodySmall: TextStyle
        @Composable get() = TextStyle(
            fontFamily = LocalAetherFonts.current.inter,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 20.sp,
            letterSpacing = 0.01.em,
        )

    val LabelKeypadSecondary: TextStyle
        @Composable get() = TextStyle(
            fontFamily = LocalAetherFonts.current.grotesk,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp,
            letterSpacing = 0.04.em,
        )

    val LabelMonoTag: TextStyle
        @Composable get() = TextStyle(
            fontFamily = LocalAetherFonts.current.mono,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 14.sp,
            letterSpacing = 0.05.em,
        )
}

/** Material theme typography bridge so Material components inherit the design scale. */
@Composable
fun aetherTypography(): Typography = Typography(
    bodyLarge = AetherType.BodyMedium,
    bodyMedium = AetherType.BodyMedium,
    bodySmall = AetherType.BodySmall,
    labelLarge = AetherType.LabelKeypadSecondary,
    labelMedium = AetherType.LabelMonoTag,
    labelSmall = AetherType.LabelMonoTag,
    titleLarge = AetherType.HeadlineExpression,
    titleMedium = AetherType.HeadlineExpression,
    titleSmall = AetherType.BodyMedium,
    displayLarge = AetherType.HeadlineDisplayMobile,
    displayMedium = AetherType.HeadlineDisplayCompressed,
    displaySmall = AetherType.HeadlineExpression,
)
