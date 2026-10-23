package com.aether.calculator.ui.theme

import com.aether.calculator.ui.CalculatorController
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/** A stand-in for the platform's storage. */
private class FakeThemeStore(var value: String? = null) : ThemeStore {
    override fun read(): String? = value
    override fun write(key: String) {
        value = key
    }
}

class ThemeTest {

    @Test
    fun midnightIsTheDefaultTheme() {
        assertEquals("midnight", AetherThemes.default.key)
    }

    @Test
    fun everyThemeHasItsOwnKey() {
        val keys = AetherThemes.all.map { it.key }
        assertEquals(keys.size, keys.toSet().size)
        assertTrue(keys.isNotEmpty())
    }

    @Test
    fun themesAreFoundByTheirKey() {
        AetherThemes.all.forEach { palette ->
            assertEquals(palette, AetherThemes.byKey(palette.key))
        }
    }

    @Test
    fun anUnknownKeyFallsBackToTheDefault() {
        assertEquals(AetherThemes.default, AetherThemes.byKey("neon"))
        assertEquals(AetherThemes.default, AetherThemes.byKey(null))
    }

    @Test
    fun applyingAPaletteChangesTheColours() {
        AetherColors.apply(AetherThemes.Midnight)
        val before = AetherColors.Primary
        AetherColors.apply(AetherThemes.Ember)
        assertNotEquals(before, AetherColors.Primary)
        assertEquals(AetherThemes.Ember.primary.base, AetherColors.Primary)
        assertEquals(AetherThemes.Ember.surfaces.surface, AetherColors.Surface)
        AetherColors.apply(AetherThemes.Midnight)
    }

    @Test
    fun aRequestOnlyMovesTheTarget() {
        AetherColors.apply(AetherThemes.Midnight)
        AetherColors.request(AetherThemes.Solar)
        assertEquals(AetherThemes.Midnight, AetherColors.current)
        assertEquals(AetherThemes.Solar, AetherColors.targetPalette)
        AetherColors.apply(AetherThemes.Midnight)
    }

    @Test
    fun blendingStartsOnTheFirstPalette() {
        val blended = AetherThemes.Midnight.blend(AetherThemes.Solar, 0f)
        assertEquals(AetherThemes.Midnight.primary.base, blended.primary.base)
        assertEquals(AetherThemes.Midnight.surfaces.surface, blended.surfaces.surface)
    }

    @Test
    fun blendingEndsOnTheSecondPalette() {
        val blended = AetherThemes.Midnight.blend(AetherThemes.Solar, 1f)
        assertEquals(AetherThemes.Solar.primary.base, blended.primary.base)
        assertEquals(AetherThemes.Solar.surfaces.surface, blended.surfaces.surface)
    }

    @Test
    fun everyThemeKeepsItsOwnAccents() {
        val accents = AetherThemes.all.map { it.primary.base }
        assertEquals(accents.size, accents.toSet().size)
    }

    @Test
    fun theControllerStartsOnThePaletteThatIsOnScreen() {
        AetherColors.apply(AetherThemes.Aurora)
        val controller = CalculatorController()
        assertEquals("aurora", controller.themeKey)
        assertEquals(AetherThemes.Aurora, controller.theme)
        AetherColors.apply(AetherThemes.Midnight)
    }

    @Test
    fun choosingAThemeIsRemembered() {
        val store = FakeThemeStore()
        AetherSettings.install(store)
        val controller = CalculatorController()
        controller.selectTheme("ember")
        assertEquals("ember", store.value)
        assertEquals("ember", controller.themeKey)
    }

    @Test
    fun aStoredThemeIsRestoredOnStartUp() {
        AetherSettings.install(FakeThemeStore("solar"))
        assertEquals(AetherThemes.Solar, AetherColors.current)
        AetherSettings.install(FakeThemeStore())
        AetherColors.apply(AetherThemes.Midnight)
    }
}
