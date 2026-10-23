package com.aether.calculator.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aether.calculator.ui.CalculatorController
import com.aether.calculator.ui.theme.AetherColors
import com.aether.calculator.ui.theme.AetherPalette
import com.aether.calculator.ui.theme.AetherShapes
import com.aether.calculator.ui.theme.AetherThemes
import com.aether.calculator.ui.theme.AetherType

/**
 * The settings sheet.
 *
 * It slides up over the calculator like the calculation log does. The theme list is the
 * first thing in it: picking a theme repaints the sheet and the calculator underneath it
 * at the same time, so the choice can be judged while it is being made.
 */
@Composable
fun SettingsSheet(controller: CalculatorController) {
    AnimatedVisibility(
        visible = controller.showSettings,
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
                    .clickableNoRipple { controller.showSettings = false }
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
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
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null,
                        tint = AetherColors.Primary,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Settings",
                            style = AetherType.HeadlineExpression.copy(fontWeight = FontWeight.Bold),
                            color = AetherColors.OnSurface,
                        )
                        Text(
                            text = "Theme · ${controller.theme.label}",
                            style = AetherType.LabelMonoTag,
                            color = AetherColors.OnSurfaceVariant.copy(alpha = 0.75f),
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    CircleIconButton(onClick = { controller.showSettings = false }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close settings",
                            tint = AetherColors.OnSurfaceVariant,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(AetherColors.OutlineVariant.copy(alpha = 0.2f))
                )
                // Theme
                Row(
                    modifier = Modifier.padding(start = 2.dp, top = 14.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Palette,
                        contentDescription = null,
                        tint = AetherColors.OnSurfaceVariant,
                        modifier = Modifier.size(15.dp),
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "THEME",
                        style = AetherType.LabelMonoTag,
                        color = AetherColors.OnSurfaceVariant.copy(alpha = 0.7f),
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AetherThemes.all.forEach { palette ->
                        ThemeCard(
                            palette = palette,
                            selected = palette.key == controller.themeKey,
                        ) {
                            controller.selectTheme(palette.key)
                        }
                    }
                }
                TactileButton(
                    onClick = { controller.showSettings = false },
                    shape = AetherShapes.Full,
                    background = AetherColors.SurfaceContainerHigh,
                    pressedScale = 0.96f,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Done",
                        style = AetherType.BodyMedium,
                        color = AetherColors.OnSurface,
                        modifier = Modifier.padding(vertical = 11.dp),
                    )
                }
            }
        }
    }
}

/**
 * One theme in the list.
 *
 * The preview shows the colours of the theme it stands for, not the ones currently on
 * screen, so every entry looks like the theme it will turn the app into.
 */
@Composable
private fun ThemeCard(
    palette: AetherPalette,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val background by animateColorAsState(
        targetValue = if (selected) {
            palette.primary.container.copy(alpha = 0.22f)
        } else {
            AetherColors.SurfaceContainer
        },
        animationSpec = tween(durationMillis = 220),
        label = "themeCardBackground",
    )
    val border by animateColorAsState(
        targetValue = if (selected) {
            palette.primary.base.copy(alpha = 0.6f)
        } else {
            AetherColors.OutlineVariant.copy(alpha = 0.2f)
        },
        animationSpec = tween(durationMillis = 220),
        label = "themeCardBorder",
    )

    TactileButton(
        onClick = onClick,
        shape = AetherShapes.Default,
        background = background,
        borderColor = border,
        pressedScale = 0.98f,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            // Preview: the accent ramp of the theme over its own surface.
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(palette.surfaces.surfaceContainerHighest)
                    .border(
                        width = 1.dp,
                        color = palette.surfaces.outline.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(14.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    palette.primary.base,
                                    palette.secondary.base,
                                    palette.tertiary.base,
                                )
                            )
                        )
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = palette.label,
                    style = AetherType.BodyMedium,
                    color = AetherColors.OnSurface,
                )
                Text(
                    text = palette.tagline,
                    style = AetherType.LabelMonoTag,
                    color = AetherColors.OnSurfaceVariant.copy(alpha = 0.7f),
                )
            }
            Spacer(Modifier.width(8.dp))
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(palette.primary.base),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        tint = palette.primary.onBase,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
    }
}
