package com.aether.calculator.core

/** A single line of the calculation log. */
data class HistoryEntry(
    val expression: String,
    val result: String,
    val timeLabel: String,
)
