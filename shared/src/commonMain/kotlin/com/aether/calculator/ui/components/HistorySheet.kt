package com.aether.calculator.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aether.calculator.core.HistoryEntry
import com.aether.calculator.ui.CalculatorController
import com.aether.calculator.ui.theme.AetherColors
import com.aether.calculator.ui.theme.AetherShapes
import com.aether.calculator.ui.theme.AetherType

/**
 * The frosted calculation log that slides up over the calculator.
 */
@Composable
fun HistorySheet(controller: CalculatorController) {
    AnimatedVisibility(
        visible = controller.showHistory,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AetherColors.Surface.copy(alpha = 0.8f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickableNoRipple { controller.showHistory = false }
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.82f)
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
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.History,
                        contentDescription = null,
                        tint = AetherColors.Primary,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Calculation Log",
                        style = AetherType.HeadlineExpression.copy(fontWeight = FontWeight.Bold),
                        color = AetherColors.OnSurface,
                    )
                    Spacer(Modifier.weight(1f))
                    TactileButton(
                        onClick = { controller.clearHistory() },
                        shape = AetherShapes.Full,
                        background = AetherColors.ErrorContainer.copy(alpha = 0.3f),
                        pressedScale = 0.92f,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null,
                                tint = AetherColors.Error,
                                modifier = Modifier.size(14.dp),
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Clear All",
                                style = AetherType.LabelMonoTag,
                                color = AetherColors.Error,
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(AetherColors.OutlineVariant.copy(alpha = 0.2f))
                )
                // Entries
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (controller.history.isEmpty()) {
                        Text(
                            text = "No calculations recorded yet",
                            style = AetherType.BodySmall,
                            color = AetherColors.OnSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                        )
                    } else {
                        controller.history.forEach { entry -> HistoryCard(entry) { controller.loadFromHistory(entry) } }
                    }
                }
                TactileButton(
                    onClick = { controller.showHistory = false },
                    shape = AetherShapes.Full,
                    background = AetherColors.SurfaceContainerHigh,
                    pressedScale = 0.96f,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Close History",
                        style = AetherType.BodyMedium,
                        color = AetherColors.OnSurface,
                        modifier = Modifier.padding(vertical = 11.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryCard(entry: HistoryEntry, onClick: () -> Unit) {
    TactileButton(
        onClick = onClick,
        shape = AetherShapes.Default,
        background = AetherColors.SurfaceContainer,
        borderColor = AetherColors.OutlineVariant.copy(alpha = 0.2f),
        pressedScale = 0.98f,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = entry.timeLabel,
                style = AetherType.LabelMonoTag,
                color = AetherColors.OnSurfaceVariant.copy(alpha = 0.7f),
            )
            Text(
                text = entry.expression,
                style = AetherType.BodySmall.copy(fontFamily = AetherType.HeadlineExpression.fontFamily),
                color = AetherColors.OnSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp),
            )
            Text(
                text = "= ${entry.result}",
                style = AetherType.HeadlineExpression.copy(fontWeight = FontWeight.Bold),
                color = AetherColors.Primary,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}
