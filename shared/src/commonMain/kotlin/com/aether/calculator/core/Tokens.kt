package com.aether.calculator.core

/**
 * A single element of the calculator's expression tape.
 *
 * The tape is the shared source of truth for the keypad, the display viewport and the
 * evaluator, so every platform renders exactly the same expression.
 */
sealed interface Tok {

    /**
     * A literal number as typed by the user, e.g. `1250` or `1.15`.
     *
     * [origin] records how the value was produced when it did not come straight from the
     * keyboard, e.g. `sin(60)`. It is only ever shown in the log: the value in [text] is
     * what gets evaluated, so the origin never changes the arithmetic.
     */
    data class Num(
        val text: String,
        val percent: Boolean = false,
        val origin: String? = null,
    ) : Tok

    /** A binary operator: `+ - * / ^`. */
    data class Op(val sym: Char) : Tok

    /** A function that consumes the operand that follows it, e.g. `sin`. */
    data class Fn(val name: String) : Tok

    data object LParen : Tok

    data object RParen : Tok

    /** A named constant: `π` or `e`. */
    data class Const(val symbol: String) : Tok
}

/** Angle unit used by the trigonometric functions. */
enum class AngleMode { RAD, DEG }
