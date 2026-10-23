package com.aether.calculator.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.LocalIndication

/**
 * A button that compresses under the thumb and springs back, matching the tactile
 * behaviour described in the design system.
 *
 * @param glow optional halo colour projected behind the button.
 */
@Composable
fun TactileButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape,
    background: Color = Color.Transparent,
    borderColor: Color? = null,
    borderWidth: Dp = 1.dp,
    glow: Color? = null,
    pressedScale: Float = 0.94f,
    content: @Composable BoxScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 900f),
        label = "keyScale",
    )
    val indication = LocalIndication.current
    val density = LocalDensity.current

    Box(
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        contentAlignment = Alignment.Center,
    ) {
        // The halo sits behind the key and is allowed to bleed past its outline.
        if (glow != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer { scaleX = 1.35f; scaleY = 1.35f }
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(glow.copy(alpha = 0.55f), Color.Transparent),
                            radius = with(density) { 140.dp.toPx() },
                        ),
                        shape = shape,
                    )
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(shape)
                .background(background)
                .then(
                    if (borderColor != null) {
                        Modifier.border(borderWidth, borderColor, shape)
                    } else {
                        Modifier
                    }
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = indication,
                    onClick = onClick,
                ),
        )
        content()
    }
}

/** A clickable without the platform ripple, used by the segmented control. */
@Composable
fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this.clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick,
    )
}

/** A small round icon button used by the top bar and the drawers. */
@Composable
fun CircleIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    content: @Composable () -> Unit,
) {
    TactileButton(
        onClick = onClick,
        modifier = modifier.size(size),
        shape = androidx.compose.foundation.shape.CircleShape,
        pressedScale = 0.9f,
        content = { content() },
    )
}

/** Rounded badge used for the history counter. */
@Composable
fun CounterBadge(count: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(16.dp)
            .clip(androidx.compose.foundation.shape.CircleShape)
            .background(com.aether.calculator.ui.theme.AetherColors.PrimaryContainer)
            .border(
                width = 2.dp,
                color = com.aether.calculator.ui.theme.AetherColors.Surface,
                shape = androidx.compose.foundation.shape.CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.material3.Text(
            text = count.toString(),
            color = com.aether.calculator.ui.theme.AetherColors.OnPrimaryContainer,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            ),
        )
    }
}
