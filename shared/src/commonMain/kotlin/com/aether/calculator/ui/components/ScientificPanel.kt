package com.aether.calculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.aether.calculator.ui.CalculatorController
import com.aether.calculator.ui.theme.AetherColors
import com.aether.calculator.ui.theme.AetherShapes
import com.aether.calculator.ui.theme.AetherType

private val ScientificKeys = listOf(
    "sin" to { c: CalculatorController -> c.applyFunction("sin") },
    "cos" to { c: CalculatorController -> c.applyFunction("cos") },
    "tan" to { c: CalculatorController -> c.applyFunction("tan") },
    "log" to { c: CalculatorController -> c.applyFunction("log") },
    "ln" to { c: CalculatorController -> c.applyFunction("ln") },
    "√" to { c: CalculatorController -> c.applyFunction("sqrt") },
    "π" to { c: CalculatorController -> c.applyConstant(com.aether.calculator.core.Expression.PI_SYMBOL) },
    "e" to { c: CalculatorController -> c.applyConstant("e") },
    "xʸ" to { c: CalculatorController -> c.appendPower() },
    "x!" to { c: CalculatorController -> c.applyFactorial() },
)

/**
 * The sliding scientific matrix: five columns of secondary functions.
 */
@Composable
fun ScientificPanel(controller: CalculatorController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(AetherShapes.Default)
            .background(AetherColors.SurfaceContainerLow)
            .border(1.dp, AetherColors.OutlineVariant.copy(alpha = 0.3f), AetherShapes.Default)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ScientificKeys.chunked(5).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                row.forEach { (label, action) ->
                    TactileButton(
                        onClick = { action(controller) },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = CircleShape,
                        background = AetherColors.SurfaceContainer,
                        pressedScale = 0.92f,
                    ) {
                        Text(
                            text = label,
                            style = AetherType.LabelKeypadSecondary,
                            color = AetherColors.Tertiary,
                        )
                    }
                }
            }
        }
    }
}
