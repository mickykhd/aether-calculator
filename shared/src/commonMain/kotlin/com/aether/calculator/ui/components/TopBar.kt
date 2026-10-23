package com.aether.calculator.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.aether.calculator.ui.CalcMode
import com.aether.calculator.ui.CalculatorController
import com.aether.calculator.ui.theme.AetherColors
import com.aether.calculator.ui.theme.AetherType

/**
 * The top app bar: brand mark, title and the three global actions.
 *
 * The bar keeps clear of a centred punch hole / floating island. A camera cutout that
 * hangs below the status bar would otherwise collide with the title, so the bar drops by
 * exactly the overhang on devices that have one.
 */
@Composable
fun TopBar(controller: CalculatorController, modifier: Modifier = Modifier) {
    val islandOverhang = rememberIslandOverhang()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp + islandOverhang,
                bottom = 6.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(AetherColors.PrimaryContainer.copy(alpha = 0.2f))
                .border(1.dp, AetherColors.Primary.copy(alpha = 0.3f), RoundedCornerShape(11.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Calculate,
                contentDescription = null,
                tint = AetherColors.Primary,
                modifier = Modifier.size(19.dp),
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Calculator",
            style = AetherType.HeadlineAppBarTitle,
            color = AetherColors.OnSurface,
        )
        Spacer(Modifier.weight(1f))
        CircleIconButton(onClick = { controller.toggleScientific() }) {
            Icon(
                imageVector = Icons.Outlined.Science,
                contentDescription = "Scientific functions",
                tint = if (controller.mode == CalcMode.SCIENTIFIC) {
                    AetherColors.Primary
                } else {
                    AetherColors.OnSurfaceVariant
                },
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(Modifier.width(4.dp))
        Box {
            CircleIconButton(onClick = { controller.showHistory = true }) {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = "Calculation history",
                    tint = AetherColors.OnSurfaceVariant,
                    modifier = Modifier.size(22.dp),
                )
            }
            if (controller.history.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 1.dp, end = 1.dp)
                ) {
                    CounterBadge(controller.history.size)
                }
            }
        }
        Spacer(Modifier.width(4.dp))
        KebabButton(
            open = controller.showMenu,
            onClick = { controller.showMenu = !controller.showMenu },
        )
    }
}

/**
 * The overflow button.
 *
 * The three dots turn into a cross while the menu is open, so the button always shows
 * what the next tap will do.
 */
@Composable
private fun KebabButton(open: Boolean, onClick: () -> Unit) {
    val turn by animateFloatAsState(
        targetValue = if (open) 1f else 0f,
        animationSpec = tween(durationMillis = 220),
        label = "kebabTurn",
    )
    val tint by animateColorAsState(
        targetValue = if (open) AetherColors.Primary else AetherColors.OnSurfaceVariant,
        animationSpec = tween(durationMillis = 200),
        label = "kebabTint",
    )
    CircleIconButton(onClick = onClick) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = if (open) null else "Modes and tools",
                tint = tint,
                modifier = Modifier
                    .size(22.dp)
                    .graphicsLayer {
                        alpha = 1f - turn
                        rotationZ = turn * 90f
                    },
            )
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = if (open) "Close menu" else null,
                tint = tint,
                modifier = Modifier
                    .size(22.dp)
                    .graphicsLayer {
                        alpha = turn
                        rotationZ = (turn - 1f) * 90f
                    },
            )
        }
    }
}
