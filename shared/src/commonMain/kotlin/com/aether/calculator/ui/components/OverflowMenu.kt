package com.aether.calculator.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.aether.calculator.core.BUILD_TAG
import com.aether.calculator.ui.CalcMode
import com.aether.calculator.ui.CalculatorController
import com.aether.calculator.ui.theme.AetherColors
import com.aether.calculator.ui.theme.AetherType

/**
 * The overflow menu behind the kebab button.
 *
 * It hangs directly under the button instead of sliding in from the edge of the screen,
 * which keeps the eye on the control that was tapped. It grows out of the top right
 * corner, so it appears to come from the button that opened it, and its lines arrive one
 * after another rather than all at once.
 */
@Composable
fun OverflowMenu(controller: CalculatorController) {
    val overhang = rememberIslandOverhang()

    Box(modifier = Modifier.fillMaxSize()) {
        // Scrim: a tap anywhere outside the menu closes it.
        AnimatedVisibility(
            visible = controller.showMenu,
            enter = fadeIn(tween(durationMillis = 220)),
            exit = fadeOut(tween(durationMillis = 150)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(safeTopInsets())
                    .background(AetherColors.SurfaceContainerLowest.copy(alpha = 0.55f))
                    .clickableNoRipple { controller.showMenu = false }
            )
        }

        // The menu card: it unfolds from the top right corner and folds back into it.
        AnimatedVisibility(
            visible = controller.showMenu,
            enter = fadeIn(tween(durationMillis = 150)) +
                scaleIn(
                    initialScale = 0.86f,
                    animationSpec = spring(dampingRatio = 0.68f, stiffness = 900f),
                    transformOrigin = TransformOrigin(1f, 0f),
                ) +
                slideInVertically(animationSpec = tween(durationMillis = 220)) { -it / 6 },
            exit = fadeOut(tween(durationMillis = 110)) +
                scaleOut(
                    targetScale = 0.92f,
                    animationSpec = tween(durationMillis = 140),
                    transformOrigin = TransformOrigin(1f, 0f),
                ) +
                slideOutVertically(animationSpec = tween(durationMillis = 140)) { -it / 8 },
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(safeTopInsets())
                    .padding(top = 66.dp + overhang, end = 16.dp)
                    .width(240.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(AetherColors.SurfaceContainerHigh)
                    .border(
                        width = 1.dp,
                        color = AetherColors.OutlineVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(18.dp),
                    )
                    .padding(vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Stagger(0) { MenuLabel("Modes & Tools") }
                Stagger(1) {
                    MenuRow(
                        icon = Icons.Outlined.Calculate,
                        label = "Basic Calculator",
                        selected = controller.mode == CalcMode.BASIC,
                    ) {
                        controller.selectMode(CalcMode.BASIC)
                        controller.showMenu = false
                    }
                }
                Stagger(2) {
                    MenuRow(
                        icon = Icons.Outlined.Science,
                        label = "Scientific Mode",
                        selected = controller.mode == CalcMode.SCIENTIFIC,
                    ) {
                        controller.selectMode(CalcMode.SCIENTIFIC)
                        controller.showMenu = false
                    }
                }
                Stagger(3) {
                    MenuRow(
                        icon = Icons.Outlined.SwapHoriz,
                        label = "Unit Converter",
                        selected = controller.mode == CalcMode.CONVERTER,
                    ) {
                        controller.selectMode(CalcMode.CONVERTER)
                        controller.showMenu = false
                    }
                }
                Stagger(4) { MenuDivider() }
                Stagger(5) { MenuLabel("Records") }
                Stagger(6) {
                    MenuRow(
                        icon = Icons.Outlined.History,
                        label = "Calculation History",
                    ) {
                        controller.showMenu = false
                        controller.showHistory = true
                    }
                }
                Stagger(7) {
                    MenuRow(
                        icon = Icons.Outlined.Delete,
                        label = "Clear History",
                        enabled = controller.history.isNotEmpty(),
                    ) {
                        controller.clearHistory()
                        controller.showMenu = false
                    }
                }
                Stagger(8) { MenuDivider() }
                Stagger(9) { MenuLabel("Preferences") }
                Stagger(10) {
                    MenuRow(
                        icon = Icons.Outlined.Settings,
                        label = "Settings",
                        trailing = {
                            Text(
                                text = controller.theme.label,
                                style = AetherType.LabelMonoTag,
                                color = AetherColors.OnSurfaceVariant.copy(alpha = 0.6f),
                            )
                        },
                    ) {
                        controller.showMenu = false
                        controller.showSettings = true
                    }
                }
                Stagger(11) { MenuDivider() }
                // Engine status, carried over from the navigation drawer.
                Stagger(12) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Aether Engine v2.4",
                                style = AetherType.LabelMonoTag,
                                color = AetherColors.OnSurfaceVariant,
                            )
                            Spacer(Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(AetherColors.Primary)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Ready",
                                style = AetherType.LabelMonoTag,
                                color = AetherColors.Primary,
                            )
                        }
                        // The build stamp tells the user which APK is installed.
                        Text(
                            text = BUILD_TAG,
                            style = AetherType.LabelMonoTag,
                            color = AetherColors.OnSurfaceVariant.copy(alpha = 0.55f),
                            modifier = Modifier.padding(top = 3.dp),
                        )
                    }
                }
            }
        }
    }
}

/**
 * A line of the menu that arrives a short beat after the one above it.
 *
 * The menu therefore assembles itself instead of appearing all at once. The lines only
 * animate on the way in: the whole card folds away as one when the menu closes.
 */
@Composable
private fun Stagger(index: Int, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(tween(durationMillis = 130, delayMillis = 45 + index * 22)) +
            slideInVertically(
                animationSpec = tween(durationMillis = 200, delayMillis = 45 + index * 22),
            ) { it / 3 },
    ) {
        content()
    }
}

/** A small heading that separates the groups inside the menu. */
@Composable
private fun MenuLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = AetherType.LabelMonoTag,
        color = AetherColors.OnSurfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 6.dp, bottom = 4.dp),
    )
}

@Composable
private fun MenuDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .height(1.dp)
            .background(AetherColors.OutlineVariant.copy(alpha = 0.25f))
    )
}

/** One row of the menu. */
@Composable
private fun MenuRow(
    icon: ImageVector,
    label: String,
    selected: Boolean = false,
    enabled: Boolean = true,
    trailing: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    val tint = when {
        !enabled -> AetherColors.OnSurfaceVariant.copy(alpha = 0.35f)
        selected -> AetherColors.Primary
        else -> AetherColors.OnSurfaceVariant
    }
    TactileButton(
        onClick = { if (enabled) onClick() },
        shape = RoundedCornerShape(12.dp),
        background = if (selected) AetherColors.PrimaryContainer.copy(alpha = 0.22f) else Color.Transparent,
        pressedScale = 0.97f,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = label,
                style = AetherType.BodyMedium,
                color = tint,
            )
            if (trailing != null) {
                Spacer(Modifier.weight(1f))
                trailing()
            }
        }
    }
}
