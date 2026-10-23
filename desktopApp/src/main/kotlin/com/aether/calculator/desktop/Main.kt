package com.aether.calculator.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.aether.calculator.ui.App
import com.aether.calculator.ui.theme.AetherSettings

/**
 * The desktop entry point. It renders exactly the same composables as Android.
 *
 * The window keeps the proportions of the phone layout, scaled down so the whole
 * calculator fits on a laptop screen.
 */
fun main() = application {
    // The stored theme is put on screen before the first frame, so the app never
    // flashes the default colours on the way to the theme the user picked.
    AetherSettings.install(DesktopThemeStore())
    Window(
        onCloseRequest = ::exitApplication,
        title = "Aether Calculator",
        state = rememberWindowState(width = 420.dp, height = 760.dp),
    ) {
        App()
    }
}
