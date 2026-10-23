package com.aether.calculator.ui.theme

/**
 * Where the chosen theme is remembered between launches.
 *
 * Each platform provides its own store: the Android app writes to shared preferences, the
 * desktop app writes to a small file in the user's home directory. The shared UI only ever
 * talks to this interface, so it stays free of platform code.
 */
interface ThemeStore {
    /** The stored theme key, or null when the user has not chosen one yet. */
    fun read(): String?

    /** Stores a theme key. */
    fun write(key: String)
}

/**
 * The bridge between the shared UI and the platform's storage.
 *
 * The app installs its store before the first frame is drawn, which is also when the
 * stored theme is put on screen, so the app never flashes the default colours first.
 */
object AetherSettings {
    private var store: ThemeStore? = null

    /** Installs the platform store and puts the stored theme on screen. */
    fun install(store: ThemeStore) {
        this.store = store
        applyStored()
    }

    /** Puts the stored theme on screen, if there is one. */
    fun applyStored() {
        val key = store?.read() ?: return
        AetherColors.apply(AetherThemes.byKey(key))
    }

    /** Remembers the chosen theme for the next launch. */
    fun persist(key: String) {
        store?.write(key)
    }
}
