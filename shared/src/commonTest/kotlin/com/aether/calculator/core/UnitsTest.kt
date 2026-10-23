package com.aether.calculator.core

import kotlin.test.Test
import kotlin.test.assertEquals

class UnitsTest {

    private fun unit(category: UnitCategory, symbol: String): Unit =
        category.units.first { it.symbol == symbol }

    @Test
    fun convertsLength() {
        val meters = unit(UnitCategory.LENGTH, "m")
        val kilometers = unit(UnitCategory.LENGTH, "km")
        assertEquals(0.001, convertUnit(1.0, meters, kilometers), 1e-12)
        assertEquals(1000.0, convertUnit(1.0, kilometers, meters), 1e-12)
    }

    @Test
    fun convertsMass() {
        val kilograms = unit(UnitCategory.MASS, "kg")
        val grams = unit(UnitCategory.MASS, "g")
        assertEquals(1500.0, convertUnit(1.5, kilograms, grams), 1e-9)
    }

    @Test
    fun convertsTemperature() {
        val celsius = unit(UnitCategory.TEMPERATURE, "°C")
        val fahrenheit = unit(UnitCategory.TEMPERATURE, "°F")
        val kelvin = unit(UnitCategory.TEMPERATURE, "K")
        assertEquals(212.0, convertUnit(100.0, celsius, fahrenheit), 1e-9)
        assertEquals(273.15, convertUnit(0.0, celsius, kelvin), 1e-9)
        assertEquals(32.0, convertUnit(273.15, kelvin, fahrenheit), 1e-9)
    }

    @Test
    fun convertsSpeed() {
        val metersPerSecond = unit(UnitCategory.SPEED, "m/s")
        val kilometersPerHour = unit(UnitCategory.SPEED, "km/h")
        assertEquals(36.0, convertUnit(10.0, metersPerSecond, kilometersPerHour), 1e-9)
    }

    @Test
    fun everyCategoryHasAtLeastTwoUnits() {
        UnitCategory.entries.forEach { category ->
            assertEquals(true, category.units.size >= 2, "${category.label} needs two units")
        }
    }
}
