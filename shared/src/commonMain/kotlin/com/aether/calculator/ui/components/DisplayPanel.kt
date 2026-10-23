package com.aether.calculator.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.aether.calculator.core.Tok
import com.aether.calculator.ui.CalculatorController
import com.aether.calculator.ui.theme.AetherColors
import com.aether.calculator.ui.theme.AetherShapes
import com.aether.calculator.ui.theme.AetherType

/**
 * The reading canvas: audit trail, live expression, primary readout and the copy row.
 *
 * The copy row is the one part of the canvas that has to stay reachable, so it claims
 * the space it needs and the reading canvas above it takes what is left. When the
 * window is too short for the full canvas, the canvas tightens up — the audit trail
 * goes, the readout and the expression line shrink — instead of pushing the copy row
 * out of sight.
 */
@Composable
fun DisplayPanel(controller: CalculatorController, modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .clip(AetherShapes.Default),
    ) {
        // The full canvas needs about 170dp; below that it tightens up instead of
        // pushing the copy row out of sight.
        val compact = maxHeight < 175.dp
        AmbientGlow()
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom,
            ) {
                if (!compact) AuditTrail(controller.audit)
                ExpressionLine(controller, compact = compact)
                Readout(controller, compact = compact)
            }
            CopyRow(controller)
        }
    }
}

@Composable
private fun BoxScope.AmbientGlow() {
    Box(
        modifier = Modifier
            .size(224.dp)
            .align(Alignment.TopEnd)
            .offset(x = 64.dp, y = (-64).dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        AetherColors.PrimaryContainer.copy(alpha = 0.15f),
                        AetherColors.PrimaryContainer.copy(alpha = 0f),
                    ),
                ),
                shape = CircleShape,
            )
    )
    Box(
        modifier = Modifier
            .size(192.dp)
            .align(Alignment.BottomStart)
            .offset(x = (-48).dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        AetherColors.TertiaryContainer.copy(alpha = 0.1f),
                        AetherColors.TertiaryContainer.copy(alpha = 0f),
                    ),
                ),
                shape = CircleShape,
            )
    )
}

@Composable
private fun AuditTrail(text: String) {
    val scrollState = rememberScrollState()
    LaunchedEffect(text) { scrollState.scrollTo(scrollState.maxValue) }
    Text(
        text = text,
        style = AetherType.BodySmall,
        color = AetherColors.OnSurfaceVariant.copy(alpha = 0.7f),
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        textAlign = TextAlign.End,
    )
}

@Composable
private fun ExpressionLine(controller: CalculatorController, compact: Boolean) {
    val tokens = controller.expressionTokens
    val text = buildAnnotatedString {
        if (tokens.isEmpty()) {
            // Nothing pending: show the last answer once there is one.
            val line = controller.expressionLine
            if (line.isNotEmpty()) {
                withStyle(SpanStyle(color = AetherColors.OnSurfaceVariant.copy(alpha = 0.4f))) {
                    append(line)
                }
            }
        } else {
            appendExpression(tokens)
        }
    }
    val scrollState = rememberScrollState()
    LaunchedEffect(text) { scrollState.scrollTo(scrollState.maxValue) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (compact) 28.dp else 32.dp)
            .horizontalScroll(scrollState),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Text(
            text = text,
            style = AetherType.HeadlineExpression,
            maxLines = 1,
        )
    }
}

private fun AnnotatedString.Builder.appendExpression(tokens: List<Tok>) {
    var open = 0
    tokens.forEach { token ->
        when (token) {
            is Tok.Num -> {
                withStyle(SpanStyle(color = AetherColors.OnSurfaceVariant)) {
                    // A value that came from a function is shown as it was written, e.g.
                    // `sin(60)`, so the line explains where the number came from.
                    append(token.origin ?: com.aether.calculator.core.NumberFormat.group(token.text))
                }
                if (token.percent) {
                    withStyle(SpanStyle(color = AetherColors.Primary, fontWeight = FontWeight.Bold)) {
                        append("%")
                    }
                }
            }
            is Tok.Op -> {
                withStyle(SpanStyle(color = AetherColors.Primary, fontWeight = FontWeight.Bold)) {
                    append(" ")
                    append(
                        when (token.sym) {
                            '*' -> "×"
                            '/' -> "÷"
                            '-' -> "−"
                            else -> token.sym.toString()
                        }
                    )
                    append(" ")
                }
            }
            is Tok.Fn -> withStyle(SpanStyle(color = AetherColors.Tertiary)) { append(token.name) }
            Tok.LParen -> {
                withStyle(SpanStyle(color = AetherColors.OnSurfaceVariant)) { append("(") }
                open++
            }
            Tok.RParen -> {
                withStyle(SpanStyle(color = AetherColors.OnSurfaceVariant)) { append(")") }
                if (open > 0) open--
            }
            is Tok.Const -> withStyle(SpanStyle(color = AetherColors.Tertiary)) { append(token.symbol) }
        }
    }
    // Groups the engine closes implicitly are shown closed, so the line stays balanced.
    if (open > 0) {
        withStyle(SpanStyle(color = AetherColors.OnSurfaceVariant)) {
            append(")".repeat(open))
        }
    }
}

@Composable
private fun Readout(controller: CalculatorController, compact: Boolean) {
    val display = controller.displayText
    val style = when {
        display.length > 9 -> AetherType.HeadlineDisplayCompressed
        compact -> AetherType.HeadlineDisplayCompressed
        else -> AetherType.HeadlineDisplayMobile
    }
    val scrollState = rememberScrollState()
    LaunchedEffect(display) { scrollState.scrollTo(scrollState.maxValue) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = if (compact) 8.dp else 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = display,
            style = style,
            color = AetherColors.OnSurface,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(scrollState),
            textAlign = TextAlign.End,
        )
        Spacer(Modifier.width(4.dp))
        BlinkingCursor()
    }
}

@Composable
private fun BlinkingCursor() {
    val transition = rememberInfiniteTransition(label = "cursor")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "cursorAlpha",
    )
    Box(
        modifier = Modifier
            .width(4.dp)
            .height(36.dp)
            .clip(CircleShape)
            .background(AetherColors.PrimaryContainer.copy(alpha = alpha))
    )
}

@Composable
private fun CopyRow(controller: CalculatorController) {
    val clipboard = LocalClipboardManager.current
    Column(modifier = Modifier.fillMaxWidth()) {
        // A hairline separates the readout from the row under it. The design draws a rule
        // here, not a box around the row, so the copy pill and the live preview sit on
        // the line rather than inside a frame they would touch.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(AetherColors.OutlineVariant.copy(alpha = 0.2f))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TactileButton(
                onClick = {
                    clipboard.setText(AnnotatedString(controller.displayText))
                    controller.copyLabel = "Copied!"
                    controller.showToast("Copied: ${controller.displayText}")
                },
                shape = AetherShapes.Full,
                background = AetherColors.SurfaceContainer,
                borderColor = AetherColors.OutlineVariant.copy(alpha = 0.3f),
                pressedScale = 0.92f,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = null,
                        tint = AetherColors.OnSurfaceVariant,
                        modifier = Modifier.size(15.dp),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = controller.copyLabel,
                        style = AetherType.LabelMonoTag,
                        color = AetherColors.OnSurfaceVariant,
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = "= ${controller.livePreview}",
                style = AetherType.BodyMedium.copy(
                    fontFamily = AetherType.HeadlineExpression.fontFamily,
                    fontWeight = FontWeight.Medium,
                ),
                color = AetherColors.PrimaryFixedDim.copy(alpha = 0.9f),
                maxLines = 1,
            )
        }
    }
}
