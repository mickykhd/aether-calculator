package com.aether.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aether.calculator.ui.App
import com.aether.calculator.ui.theme.AetherSettings

/**
 * The Android entry point. Every pixel of the UI comes from the shared KMP module.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // The stored theme is put on screen before the first frame, so the app never
        // flashes the default colours on the way to the theme the user picked.
        AetherSettings.install(AndroidThemeStore(this))
        setContent {
            App()
        }
    }
}
