package com.aether.calculator.core

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * Number formatting helpers shared by every platform.
 *
 * The design calls for monospaced readouts with thousands separators, so the same
 * grouping rules are applied to typed entries, live previews and history entries.
 */
object NumberFormat {

    /** Inserts thousands separators into the integer part of a plain decimal literal. */
    fun group(text: String): String {
        if (text.isEmpty()) return text
        val negative = text.startsWith("-")
        val body = if (negative) text.substring(1) else text
        val dot = body.indexOf('.')
        val integerPart = if (dot >= 0) body.substring(0, dot) else body
        val fractionPart = if (dot >= 0) body.substring(dot) else ""
        if (integerPart.isEmpty() || !integerPart.all { it.isDigit() }) return text
        val grouped = integerPart.reversed().chunked(3).joinToString(",").reversed()
        return (if (negative) "-" else "") + grouped + fractionPart
    }

    /** Formats a double without exponent notation and without trailing zeroes. */
    fun plain(value: Double): String {
        if (value.isNaN()) return "NaN"
        if (value.isInfinite()) return if (value > 0) "Infinity" else "-Infinity"
        if (value == 0.0) return "0"

        val magnitude = abs(value)
        if (magnitude >= 1e12 || magnitude < 1e-9) return exponential(value)

        val decimals = when {
            magnitude >= 1e9 -> 0
            magnitude >= 1e6 -> 2
            magnitude >= 1e3 -> 4
            else -> 8
        }
        var factor = 1.0
        repeat(decimals) { factor *= 10 }
        val scaled = (value * factor).roundToLong()
        if (scaled == 0L) return "0"

        val negative = scaled < 0
        val digits = (if (negative) -scaled else scaled).toString()
        val body = if (decimals == 0) {
            digits
        } else {
            val padded = digits.padStart(decimals + 1, '0')
            val integerPart = padded.dropLast(decimals)
            val fractionPart = padded.takeLast(decimals).trimEnd('0')
            if (fractionPart.isEmpty()) integerPart else "$integerPart.$fractionPart"
        }
        return (if (negative) "-" else "") + body
    }

    /** Formats a double with thousands separators for the readouts. */
    fun display(value: Double): String = group(plain(value))

    /** Formats a literal as typed by the user, keeping the user's own precision. */
    fun displayTyped(text: String): String = group(text)

    private fun exponential(value: Double): String {
        val exponent = floor(log10(abs(value))).toInt()
        val mantissa = value / 10.0.pow(exponent.toDouble())
        val mantissaText = plain(mantissa)
        val sign = if (exponent >= 0) "+" else "-"
        return "$mantissaText E$sign${abs(exponent)}"
    }
}
