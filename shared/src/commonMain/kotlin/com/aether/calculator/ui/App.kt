package com.aether.calculator.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aether.calculator.ui.components.ConverterPanel
import com.aether.calculator.ui.components.DisplayPanel
import com.aether.calculator.ui.components.HistorySheet
import com.aether.calculator.ui.components.Keypad
import com.aether.calculator.ui.components.ModeSwitcher
import com.aether.calculator.ui.components.OverflowMenu
import com.aether.calculator.ui.components.ScientificPanel
import com.aether.calculator.ui.components.SettingsSheet
import com.aether.calculator.ui.components.ToastHost
import com.aether.calculator.ui.components.TopBar
import com.aether.calculator.ui.components.UnitPickerSheet
import com.aether.calculator.ui.components.safeTopInsets
import com.aether.calculator.ui.theme.AetherColors
import com.aether.calculator.ui.theme.AetherTheme
import kotlinx.coroutines.delay

/**
 * The application shell. It is shared verbatim by the Android and Desktop entry points.
 */
@Composable
fun App() {
    AetherTheme {
        val controller = remember { CalculatorController() }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AetherColors.Background),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(safeTopInsets())
                    .navigationBarsPadding()
                    .imePadding(),
            ) {
                TopBar(controller)
                ModeSwitcher(controller)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    // The reading canvas and the converter swap places with a transition
                    // that slides in the direction of the chosen tab.
                    AnimatedContent(
                        targetState = controller.mode,
                        transitionSpec = {
                            val forwards = targetState.ordinal > initialState.ordinal
                            (fadeIn(tween(durationMillis = 200)) +
                                slideInVertically(tween(durationMillis = 260)) {
                                    if (forwards) it / 12 else -it / 12
                                }).togetherWith(fadeOut(tween(durationMillis = 140)))
                        },
                        label = "modeContent",
                    ) { mode ->
                        when (mode) {
                            CalcMode.BASIC, CalcMode.SCIENTIFIC -> DisplayPanel(controller)
                            CalcMode.CONVERTER -> ConverterPanel(controller)
                        }
                    }
                }

                // The scientific matrix unfolds below the display.
                AnimatedVisibility(
                    visible = controller.mode == CalcMode.SCIENTIFIC,
                    enter = fadeIn(tween(durationMillis = 200)) +
                        expandVertically(tween(durationMillis = 260)) +
                        slideInVertically(tween(durationMillis = 260)) { it / 3 },
                    exit = fadeOut(tween(durationMillis = 130)) +
                        shrinkVertically(tween(durationMillis = 200)) +
                        slideOutVertically(tween(durationMillis = 200)) { it / 3 },
                ) {
                    ScientificPanel(controller)
                }

                Keypad(controller)
            }

            // Overlays
            UnitPickerSheet(controller)
            HistorySheet(controller)
            SettingsSheet(controller)
            OverflowMenu(controller)
            ToastHost(controller)
        }

        // Auto-dismiss the toast after a short delay.
        LaunchedEffect(controller.toastMessage) {
            if (controller.toastMessage != null) {
                delay(1800)
                controller.dismissToast()
            }
        }
    }
}
