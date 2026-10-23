package com.aether.calculator

import android.content.Context
import com.aether.calculator.ui.theme.ThemeStore

/**
 * Remembers the chosen theme in the app's shared preferences.
 */
class AndroidThemeStore(context: Context) : ThemeStore {
    private val preferences = context.getSharedPreferences("aether_theme", Context.MODE_PRIVATE)

    override fun read(): String? = preferences.getString(KEY, null)

    override fun write(key: String) {
        preferences.edit().putString(KEY, key).apply()
    }

    private companion object {
        const val KEY = "theme"
    }
}
