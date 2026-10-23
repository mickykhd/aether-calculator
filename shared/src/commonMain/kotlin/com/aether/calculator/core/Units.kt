package com.aether.calculator.core

/** A measurable unit with its conversion into (and out of) the category's base unit. */
class Unit(
    val name: String,
    val symbol: String,
    private val toBase: (Double) -> Double,
    private val fromBase: (Double) -> Double,
) {
    fun toBaseValue(value: Double): Double = toBase(value)

    fun fromBaseValue(value: Double): Double = fromBase(value)
}

/** A group of units that can be converted into one another. */
enum class UnitCategory(val label: String, val units: List<Unit>) {
    LENGTH(
        "Length",
        listOf(
            linearUnit("Meter", "m", 1.0),
            linearUnit("Kilometer", "km", 1_000.0),
            linearUnit("Centimeter", "cm", 0.01),
            linearUnit("Millimeter", "mm", 0.001),
            linearUnit("Mile", "mi", 1_609.344),
            linearUnit("Yard", "yd", 0.9144),
            linearUnit("Foot", "ft", 0.3048),
            linearUnit("Inch", "in", 0.0254),
            linearUnit("Nautical mile", "nmi", 1_852.0),
        )
    ),
    MASS(
        "Mass",
        listOf(
            linearUnit("Kilogram", "kg", 1.0),
            linearUnit("Gram", "g", 0.001),
            linearUnit("Milligram", "mg", 0.000_001),
            linearUnit("Metric ton", "t", 1_000.0),
            linearUnit("Pound", "lb", 0.453_592_37),
            linearUnit("Ounce", "oz", 0.028_349_523_125),
            linearUnit("Stone", "st", 6.350_293_18),
        )
    ),
    TEMPERATURE(
        "Temperature",
        listOf(
            Unit("Celsius", "°C", { it + 273.15 }, { it - 273.15 }),
            Unit("Fahrenheit", "°F", { (it - 32.0) * 5.0 / 9.0 + 273.15 }, { (it - 273.15) * 9.0 / 5.0 + 32.0 }),
            Unit("Kelvin", "K", { it }, { it }),
        )
    ),
    AREA(
        "Area",
        listOf(
            linearUnit("Square meter", "m²", 1.0),
            linearUnit("Square kilometer", "km²", 1_000_000.0),
            linearUnit("Square centimeter", "cm²", 0.0001),
            linearUnit("Square foot", "ft²", 0.092_903_04),
            linearUnit("Square inch", "in²", 0.000_645_16),
            linearUnit("Square mile", "mi²", 2_589_988.110_336),
            linearUnit("Acre", "ac", 4_046.856_422_4),
            linearUnit("Hectare", "ha", 10_000.0),
        )
    ),
    VOLUME(
        "Volume",
        listOf(
            linearUnit("Liter", "L", 1.0),
            linearUnit("Milliliter", "mL", 0.001),
            linearUnit("Cubic meter", "m³", 1_000.0),
            linearUnit("Cubic centimeter", "cm³", 0.001),
            linearUnit("US gallon", "gal", 3.785_411_784),
            linearUnit("US quart", "qt", 0.946_352_946),
            linearUnit("US pint", "pt", 0.473_176_473),
            linearUnit("Fluid ounce", "fl oz", 0.029_573_529_562_5),
        )
    ),
    SPEED(
        "Speed",
        listOf(
            linearUnit("Meter per second", "m/s", 1.0),
            linearUnit("Kilometer per hour", "km/h", 1.0 / 3.6),
            linearUnit("Mile per hour", "mph", 0.447_04),
            linearUnit("Foot per second", "ft/s", 0.3048),
            linearUnit("Knot", "kn", 0.514_444_444_444_4),
        )
    ),
    TIME(
        "Time",
        listOf(
            linearUnit("Second", "s", 1.0),
            linearUnit("Minute", "min", 60.0),
            linearUnit("Hour", "h", 3_600.0),
            linearUnit("Day", "d", 86_400.0),
            linearUnit("Week", "wk", 604_800.0),
            linearUnit("Month", "mo", 2_629_800.0),
            linearUnit("Year", "yr", 31_557_600.0),
        )
    ),
    DATA(
        "Data",
        listOf(
            linearUnit("Byte", "B", 1.0),
            linearUnit("Kilobyte", "KB", 1_000.0),
            linearUnit("Megabyte", "MB", 1_000_000.0),
            linearUnit("Gigabyte", "GB", 1_000_000_000.0),
            linearUnit("Terabyte", "TB", 1_000_000_000_000.0),
            linearUnit("Kibibyte", "KiB", 1_024.0),
            linearUnit("Mebibyte", "MiB", 1_048_576.0),
            linearUnit("Gibibyte", "GiB", 1_073_741_824.0),
        )
    );

}

/** Builds a unit that scales linearly against the category base unit. */
private fun linearUnit(name: String, symbol: String, factor: Double): Unit =
    Unit(name, symbol, { it * factor }, { it / factor })

/** Converts a value between two units of the same category. */
fun convertUnit(value: Double, from: Unit, to: Unit): Double =
    to.fromBaseValue(from.toBaseValue(value))
