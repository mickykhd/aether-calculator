package com.aether.calculator.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aether.calculator.core.NumberFormat
import com.aether.calculator.core.UnitCategory
import com.aether.calculator.ui.CalculatorController
import com.aether.calculator.ui.ConverterTarget
import com.aether.calculator.ui.theme.AetherColors
import com.aether.calculator.ui.theme.AetherShapes
import com.aether.calculator.ui.theme.AetherType

/**
 * The way a value or a unit symbol changes when it is replaced by another one.
 *
 * The new text rises while the old one sinks, so the two never lie on top of each
 * other while they trade places.
 */
private val ValueEnter = fadeIn(tween(200)) + slideInVertically(tween(240)) { it / 3 }
private val ValueExit = fadeOut(tween(140)) + slideOutVertically(tween(200)) { -it / 3 }

/**
 * The unit converter that replaces the reading canvas in converter mode.
 *
 * Every change the user makes — a new unit, a new category, a swap — moves the numbers
 * into place instead of snapping them, so it is always clear which value belongs to
 * which unit.
 *
 * The panel is taller than the canvas on a short window, so it scrolls: the card the
 * user has not reached yet is never hidden behind the keypad.
 */
@Composable
fun ConverterPanel(controller: CalculatorController, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        // A short canvas gets a tighter layout, so the two cards and the swap button
        // fit without scrolling where they can.
        val compact = maxHeight < 260.dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 12.dp),
        ) {
            CategoryChips(controller, compact)
            ValueCard(
                label = "FROM",
                value = NumberFormat.displayTyped(controller.converterValue),
                unit = controller.converterCategory.units[controller.converterFromIndex],
                valueColor = AetherColors.OnSurface,
                compact = compact,
                onPickUnit = { controller.converterPicker = ConverterTarget.FROM },
            )
            SwapButton(controller, compact)
            ValueCard(
                label = "TO",
                value = controller.converterResult,
                unit = controller.converterCategory.units[controller.converterToIndex],
                valueColor = AetherColors.Primary,
                compact = compact,
                onPickUnit = { controller.converterPicker = ConverterTarget.TO },
            )
        }
    }
}

/**
 * The button between the two cards.
 *
 * It turns as it is pressed, so the swap is visible and not just a change of numbers.
 */
@Composable
private fun SwapButton(controller: CalculatorController, compact: Boolean) {
    var turned by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (turned) 180f else 0f,
        animationSpec = tween(durationMillis = 320),
        label = "swapRotation",
    )
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        TactileButton(
            onClick = {
                turned = !turned
                controller.swapConverterUnits()
            },
            shape = CircleShape,
            background = AetherColors.SurfaceContainerHigh,
            borderColor = AetherColors.OutlineVariant.copy(alpha = 0.3f),
            pressedScale = 0.9f,
            modifier = Modifier.size(if (compact) 32.dp else 40.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.SwapHoriz,
                contentDescription = "Swap units",
                tint = AetherColors.OnSurfaceVariant,
                modifier = Modifier
                    .size(20.dp)
                    .graphicsLayer { rotationZ = rotation },
            )
        }
    }
}

@Composable
private fun CategoryChips(controller: CalculatorController, compact: Boolean) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        UnitCategory.entries.forEach { category ->
            val selected = category == controller.converterCategory
            // The selection colour travels to the new chip instead of jumping.
            val background by animateColorAsState(
                targetValue = if (selected) AetherColors.PrimaryContainer else AetherColors.SurfaceContainer,
                animationSpec = tween(durationMillis = 220),
                label = "chipBackground",
            )
            val content by animateColorAsState(
                targetValue = if (selected) AetherColors.OnPrimaryContainer else AetherColors.OnSurfaceVariant,
                animationSpec = tween(durationMillis = 220),
                label = "chipContent",
            )
            TactileButton(
                onClick = { controller.selectConverterCategory(category) },
                shape = AetherShapes.Full,
                background = background,
                pressedScale = 0.92f,
            ) {
                Text(
                    text = category.label,
                    style = AetherType.LabelMonoTag,
                    color = content,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = if (compact) 4.dp else 7.dp,
                    ),
                )
            }
        }
    }
}

@Composable
private fun ValueCard(
    label: String,
    value: String,
    unit: com.aether.calculator.core.Unit,
    valueColor: Color,
    compact: Boolean,
    onPickUnit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AetherShapes.Default)
            .background(AetherColors.SurfaceContainer)
            .border(1.dp, AetherColors.OutlineVariant.copy(alpha = 0.2f), AetherShapes.Default)
            .padding(if (compact) 8.dp else 16.dp),
    ) {
        Text(
            text = label,
            style = AetherType.LabelMonoTag,
            color = AetherColors.OnSurfaceVariant.copy(alpha = 0.6f),
        )
        Spacer(Modifier.height(if (compact) 2.dp else 4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            // The value and the unit cross-fade when they are replaced.
            AnimatedContent(
                targetState = value,
                transitionSpec = { ValueEnter togetherWith ValueExit },
                label = "converterValue",
                modifier = Modifier.weight(1f),
            ) { shown ->
                Text(
                    text = shown,
                    style = if (compact) {
                        // A shorter canvas needs a shorter number.
                        AetherType.HeadlineDisplayCompressed.copy(
                            fontSize = 24.sp,
                            lineHeight = 30.sp,
                        )
                    } else {
                        AetherType.HeadlineDisplayCompressed
                    },
                    color = valueColor,
                    maxLines = 1,
                )
            }
            Spacer(Modifier.width(12.dp))
            TactileButton(
                onClick = onPickUnit,
                shape = AetherShapes.Full,
                background = AetherColors.SurfaceContainerHigh,
                pressedScale = 0.92f,
            ) {
                AnimatedContent(
                    targetState = unit.symbol,
                    transitionSpec = { ValueEnter togetherWith ValueExit },
                    label = "converterUnit",
                ) { shown ->
                    Text(
                        text = shown,
                        style = AetherType.LabelKeypadSecondary,
                        color = AetherColors.OnSurface,
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = if (compact) 5.dp else 8.dp,
                        ),
                    )
                }
            }
        }
    }
}

/**
 * The unit picker sheet, presented when a unit slot is tapped.
 *
 * It slides up over the converter and slides back down when it closes, the same way the
 * calculation log and the settings sheet do.
 */
@Composable
fun UnitPickerSheet(controller: CalculatorController) {
    val target = controller.converterPicker
    // The sheet stays composed while it slides away, so the side that was opened is
    // remembered for the exit animation.
    val lastTarget = remember { mutableStateOf<ConverterTarget?>(null) }
    SideEffect { if (target != null) lastTarget.value = target }
    // The sheet itself always stays in the composition, even while it is closed, so
    // that opening it plays the entrance rather than showing it out of nowhere.
    val shown = target ?: lastTarget.value ?: ConverterTarget.FROM

    AnimatedVisibility(
        visible = target != null,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it },
    ) {
        val units = controller.converterCategory.units
        val selectedIndex = if (shown == ConverterTarget.FROM) {
            controller.converterFromIndex
        } else {
            controller.converterToIndex
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AetherColors.Surface.copy(alpha = 0.8f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickableNoRipple { controller.converterPicker = null }
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.62f)
                    .clip(RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp))
                    .background(AetherColors.SurfaceContainerLow)
                    .border(
                        width = 1.dp,
                        color = AetherColors.OutlineVariant.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp),
                    )
                    .padding(16.dp),
            ) {
                // Grab handle
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(48.dp)
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(AetherColors.OutlineVariant.copy(alpha = 0.6f))
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = if (shown == ConverterTarget.FROM) "Convert from" else "Convert to",
                    style = AetherType.HeadlineExpression.copy(fontWeight = FontWeight.Bold),
                    color = AetherColors.OnSurface,
                )
                Spacer(Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    units.forEachIndexed { index, unit ->
                        val selected = index == selectedIndex
                        TactileButton(
                            onClick = {
                                controller.setConverterUnit(index, shown == ConverterTarget.FROM)
                                controller.converterPicker = null
                            },
                            shape = AetherShapes.Full,
                            background = if (selected) {
                                AetherColors.PrimaryContainer
                            } else {
                                AetherColors.SurfaceContainer
                            },
                            pressedScale = 0.97f,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = unit.name,
                                    style = AetherType.BodyMedium,
                                    color = if (selected) {
                                        AetherColors.OnPrimaryContainer
                                    } else {
                                        AetherColors.OnSurface
                                    },
                                )
                                Spacer(Modifier.weight(1f))
                                Text(
                                    text = unit.symbol,
                                    style = AetherType.LabelMonoTag,
                                    color = if (selected) {
                                        AetherColors.OnPrimaryContainer
                                    } else {
                                        AetherColors.OnSurfaceVariant
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
