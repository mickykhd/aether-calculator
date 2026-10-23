package com.aether.calculator.core

import kotlin.test.Test
import kotlin.test.assertEquals

class NumberFormatTest {

    @Test
    fun groupsThousands() {
        assertEquals("1,250", NumberFormat.group("1250"))
        assertEquals("1,234,567", NumberFormat.group("1234567"))
        assertEquals("1,234,567.25", NumberFormat.group("1234567.25"))
        assertEquals("-1,250", NumberFormat.group("-1250"))
    }

    @Test
    fun leavesShortNumbersAlone() {
        assertEquals("125", NumberFormat.group("125"))
        assertEquals("1.5", NumberFormat.group("1.5"))
        assertEquals("", NumberFormat.group(""))
    }

    @Test
    fun formatsWithoutTrailingZeroes() {
        assertEquals("1437.5", NumberFormat.plain(1437.5))
        assertEquals("105", NumberFormat.plain(105.0))
        assertEquals("0", NumberFormat.plain(0.0))
        assertEquals("-12.25", NumberFormat.plain(-12.25))
    }

    @Test
    fun displaysWithSeparators() {
        assertEquals("1,437.5", NumberFormat.display(1437.5))
        assertEquals("105", NumberFormat.display(105.0))
    }

    @Test
    fun switchesToExponentialOutsideTheReadableRange() {
        assertEquals(true, NumberFormat.plain(1e15).contains("E"))
        assertEquals(true, NumberFormat.plain(1e-12).contains("E"))
    }
}
