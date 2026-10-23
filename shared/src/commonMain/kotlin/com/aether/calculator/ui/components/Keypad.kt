package com.aether.calculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aether.calculator.ui.CalcMode
import com.aether.calculator.ui.CalculatorController
import com.aether.calculator.ui.theme.AetherColors
import com.aether.calculator.ui.theme.AetherShapes
import com.aether.calculator.ui.theme.AetherType

private const val KeyHeight = 64
private const val KeyGap = 12

/** The kind of a keypad key, which drives its surface, label and behaviour. */
private enum class KeyKind { NUMBER, OPERATOR, DESTRUCTIVE, SECONDARY, BACKSPACE, EQUALS }

/**
 * The ergonomic keypad matrix: four columns, five rows, twelve point gutters.
 */
@Composable
fun Keypad(controller: CalculatorController, modifier: Modifier = Modifier) {
    val converterMode = controller.mode == CalcMode.CONVERTER

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AetherColors.SurfaceContainerLow)
            .border(
                width = 1.dp,
                color = AetherColors.OutlineVariant.copy(alpha = 0.3f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp,
                ),
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(KeyGap.dp),
    ) {
        KeyRow {
            Key("AC", KeyKind.DESTRUCTIVE, Modifier.weight(1f)) {
                if (converterMode) controller.clearConverter() else controller.clearAll()
            }
            Key("( )", KeyKind.SECONDARY, Modifier.weight(1f), enabled = !converterMode) {
                controller.toggleParentheses()
            }
            Key("%", KeyKind.SECONDARY, Modifier.weight(1f), enabled = !converterMode) {
                controller.applyPercent()
            }
            Key("÷", KeyKind.OPERATOR, Modifier.weight(1f), enabled = !converterMode) {
                controller.appendOperator('/')
            }
        }
        KeyRow {
            Key("7", KeyKind.NUMBER, Modifier.weight(1f)) { digit(controller, converterMode, "7") }
            Key("8", KeyKind.NUMBER, Modifier.weight(1f)) { digit(controller, converterMode, "8") }
            Key("9", KeyKind.NUMBER, Modifier.weight(1f)) { digit(controller, converterMode, "9") }
            Key("×", KeyKind.OPERATOR, Modifier.weight(1f), enabled = !converterMode) {
                controller.appendOperator('*')
            }
        }
        KeyRow {
            Key("4", KeyKind.NUMBER, Modifier.weight(1f)) { digit(controller, converterMode, "4") }
            Key("5", KeyKind.NUMBER, Modifier.weight(1f)) { digit(controller, converterMode, "5") }
            Key("6", KeyKind.NUMBER, Modifier.weight(1f)) { digit(controller, converterMode, "6") }
            Key("−", KeyKind.OPERATOR, Modifier.weight(1f), enabled = !converterMode) {
                controller.appendOperator('-')
            }
        }
        KeyRow {
            Key("1", KeyKind.NUMBER, Modifier.weight(1f)) { digit(controller, converterMode, "1") }
            Key("2", KeyKind.NUMBER, Modifier.weight(1f)) { digit(controller, converterMode, "2") }
            Key("3", KeyKind.NUMBER, Modifier.weight(1f)) { digit(controller, converterMode, "3") }
            Key("+", KeyKind.OPERATOR, Modifier.weight(1f), enabled = !converterMode) {
                controller.appendOperator('+')
            }
        }
        KeyRow {
            Key("0", KeyKind.NUMBER, Modifier.weight(1f)) { digit(controller, converterMode, "0") }
            Key(".", KeyKind.NUMBER, Modifier.weight(1f)) {
                if (converterMode) controller.appendConverterDecimal() else controller.appendDecimal()
            }
            Key("", KeyKind.BACKSPACE, Modifier.weight(1f)) {
                if (converterMode) controller.backspaceConverter() else controller.backspace()
            }
            Key("=", KeyKind.EQUALS, Modifier.weight(1f)) {
                if (converterMode) {
                    controller.convert()
                } else {
                    controller.evaluate()
                }
            }
        }
    }
}

@Composable
private fun KeyRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(KeyGap.dp),
        content = content,
    )
}

private fun digit(controller: CalculatorController, converterMode: Boolean, value: String) {
    if (converterMode) controller.appendConverterDigit(value) else controller.appendDigit(value)
}

@Composable
private fun RowScope.Key(
    label: String,
    kind: KeyKind,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val shape = AetherShapes.Key
    val background: Color
    val labelColor: Color
    val border: Color?
    var glow: Color? = null

    when (kind) {
        KeyKind.NUMBER -> {
            background = AetherColors.SurfaceContainerHigh
            labelColor = AetherColors.OnSurface
            border = Color.White.copy(alpha = 0.04f)
        }
        KeyKind.OPERATOR -> {
            background = AetherColors.PrimaryContainer.copy(alpha = 0.2f)
            labelColor = AetherColors.PrimaryFixedDim
            border = AetherColors.PrimaryContainer.copy(alpha = 0.4f)
        }
        KeyKind.DESTRUCTIVE -> {
            background = AetherColors.SurfaceContainer
            labelColor = AetherColors.Secondary
            border = AetherColors.OutlineVariant.copy(alpha = 0.2f)
        }
        KeyKind.SECONDARY -> {
            background = AetherColors.SurfaceContainer
            labelColor = AetherColors.OnSurfaceVariant
            border = AetherColors.OutlineVariant.copy(alpha = 0.2f)
        }
        KeyKind.BACKSPACE -> {
            background = AetherColors.SurfaceContainer
            labelColor = AetherColors.OnSurfaceVariant
            border = AetherColors.OutlineVariant.copy(alpha = 0.2f)
        }
        KeyKind.EQUALS -> {
            background = AetherColors.PrimaryContainer
            labelColor = AetherColors.OnPrimaryContainer
            border = null
            glow = AetherColors.PrimaryContainer
        }
    }

    val alpha = if (enabled) 1f else 0.35f

    TactileButton(
        onClick = { if (enabled) onClick() },
        modifier = modifier.height(KeyHeight.dp),
        shape = shape,
        background = background.copy(alpha = background.alpha * alpha),
        borderColor = border?.copy(alpha = (border.alpha * alpha)),
        glow = glow?.copy(alpha = alpha),
    ) {
        if (kind == KeyKind.BACKSPACE) {
            Icon(
                imageVector = Icons.Outlined.Backspace,
                contentDescription = "Delete",
                tint = labelColor.copy(alpha = alpha),
                modifier = Modifier.size(24.dp),
            )
        } else {
            Text(
                text = label,
                color = labelColor.copy(alpha = alpha),
                style = when (kind) {
                    KeyKind.OPERATOR -> AetherType.HeadlineKeypadOperator
                    KeyKind.DESTRUCTIVE -> AetherType.HeadlineKeypadPrimary.copy(fontWeight = FontWeight.Bold)
                    KeyKind.EQUALS -> AetherType.HeadlineKeypadPrimary.copy(fontWeight = FontWeight.Bold)
                    else -> AetherType.HeadlineKeypadPrimary
                },
            )
        }
    }
}
