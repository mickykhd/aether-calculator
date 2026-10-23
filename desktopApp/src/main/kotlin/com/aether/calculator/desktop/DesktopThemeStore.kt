package com.aether.calculator.desktop

import com.aether.calculator.ui.theme.ThemeStore
import java.io.File

/**
 * Remembers the chosen theme in a small file in the user's home directory.
 */
class DesktopThemeStore : ThemeStore {
    private val file = File(
        System.getProperty("user.home") ?: ".",
        ".aether-calculator/theme.txt",
    )

    override fun read(): String? = if (file.exists()) {
        file.readText().trim().ifEmpty { null }
    } else {
        null
    }

    override fun write(key: String) {
        file.parentFile?.mkdirs()
        file.writeText(key)
    }
}
