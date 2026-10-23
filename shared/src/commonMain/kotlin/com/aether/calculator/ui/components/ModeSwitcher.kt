package com.aether.calculator.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aether.calculator.ui.CalcMode
import com.aether.calculator.ui.CalculatorController
import com.aether.calculator.ui.theme.AetherColors
import com.aether.calculator.ui.theme.AetherShapes
import com.aether.calculator.ui.theme.AetherType

private val ModeLabels = listOf(
    CalcMode.BASIC to "Basic",
    CalcMode.SCIENTIFIC to "Scientific",
    CalcMode.CONVERTER to "Converter",
)

/**
 * The quick mode bar: a segmented control plus the angle unit pill.
 */
@Composable
fun ModeSwitcher(controller: CalculatorController, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SegmentedControl(controller)
        Spacer(Modifier.weight(1f))
        AngleModePill(controller)
    }
}

@Composable
private fun SegmentedControl(controller: CalculatorController) {
    val activeIndex = ModeLabels.indexOfFirst { it.first == controller.mode }.coerceAtLeast(0)
    var widths by remember { mutableStateOf(List(ModeLabels.size) { 0f }) }
    var offsets by remember { mutableStateOf(List(ModeLabels.size) { 0f }) }
    val density = LocalDensity.current

    val indicatorOffset by animateDpAsState(
        targetValue = with(density) { offsets[activeIndex].toDp() },
        animationSpec = tween(durationMillis = 260),
        label = "segmentOffset",
    )
    val indicatorWidth by animateDpAsState(
        targetValue = with(density) { widths[activeIndex].toDp() },
        animationSpec = tween(durationMillis = 260),
        label = "segmentWidth",
    )

    Box(
        modifier = Modifier
            .clip(AetherShapes.Full)
            .background(AetherColors.SurfaceContainer)
            .border(1.dp, AetherColors.OutlineVariant.copy(alpha = 0.3f), AetherShapes.Full)
            .padding(4.dp),
    ) {
        if (widths[activeIndex] > 0) {
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(indicatorWidth)
                    .height(28.dp)
                    .clip(CircleShape)
                    .background(AetherColors.PrimaryContainer)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            ModeLabels.forEachIndexed { index, entry ->
                val (mode, label) = entry
                val selected = index == activeIndex
                Box(
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            widths = widths.toMutableList().also { it[index] = coordinates.size.width.toFloat() }
                            offsets = offsets.toMutableList().also {
                                it[index] = coordinates.positionInParent().x
                            }
                        }
                        .clip(CircleShape)
                        .clickableNoRipple { controller.selectMode(mode) }
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        style = AetherType.BodySmall.copy(fontWeight = FontWeight.Medium),
                        color = if (selected) AetherColors.OnPrimaryContainer else AetherColors.OnSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun AngleModePill(controller: CalculatorController) {
    val label = if (controller.angleMode == com.aether.calculator.core.AngleMode.RAD) "RAD" else "DEG"
    TactileButton(
        onClick = { controller.toggleAngleMode() },
        shape = AetherShapes.Full,
        background = AetherColors.TertiaryContainer.copy(alpha = 0.2f),
        borderColor = AetherColors.TertiaryContainer.copy(alpha = 0.3f),
        pressedScale = 0.92f,
        modifier = Modifier,
    ) {
        Text(
            text = label,
            style = AetherType.LabelMonoTag,
            color = AetherColors.Tertiary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        )
    }
}
