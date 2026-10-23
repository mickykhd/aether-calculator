package com.aether.calculator.core

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

/** Raised when an expression cannot be parsed or evaluated. */
class ExpressionException(message: String) : Exception(message)

/** Environment the expression is evaluated against. */
data class EvalContext(
    val ans: Double = 0.0,
    val angleMode: AngleMode = AngleMode.RAD,
)

/**
 * Recursive descent evaluator for the calculator expression tape.
 *
 * Grammar (highest precedence last):
 * ```
 * expression := term (('+' | '-') term)*
 * term       := factor (('*' | '/') factor)*
 * factor     := unary ('^' factor)?          // right associative
 * unary      := ('+' | '-') unary | postfix
 * postfix    := primary ('!' | '%')*
 * primary    := NUMBER | CONST | 'ans' | '(' expression ')' | FN '(' expression ')'
 * ```
 *
 * Percent follows the behaviour of a pocket calculator: `200 + 10%` is `220` (ten percent
 * of the running total) while `200 x 10%` is `20`.
 */
object Expression {

    fun evaluate(tokens: List<Tok>, context: EvalContext = EvalContext()): Double =
        evaluateSource(serialize(tokens), context)

    /**
     * Evaluates a raw expression such as `2 + 3 x 4` or `5!`.
     *
     * The keypad builds a [Tok] tape, but the engine also accepts plain text so pasted
     * input can be evaluated with exactly the same grammar.
     */
    fun evaluateSource(source: String, context: EvalContext = EvalContext()): Double {
        if (source.isBlank()) return 0.0
        val parser = Parser(source.trim(), context)
        val node = parser.parseExpression()
        parser.expectEnd()
        return evaluateNode(node, context)
    }

    /** Serializes the tape into the textual form the parser consumes. */
    fun serialize(tokens: List<Tok>): String {
        val sb = StringBuilder()
        for (token in tokens) {
            when (token) {
                is Tok.Num -> {
                    sb.append(token.text)
                    if (token.percent) sb.append('%')
                }
                is Tok.Op -> sb.append(token.sym)
                is Tok.Fn -> sb.append(token.name)
                Tok.LParen -> sb.append('(')
                Tok.RParen -> sb.append(')')
                is Tok.Const -> sb.append(if (token.symbol == PI_SYMBOL) "pi" else token.symbol)
            }
        }
        return sb.toString()
    }

    /**
     * Renders the tape with the glyphs used by the design (`×`, `÷`, `−`).
     *
     * Groups the user left open are closed again, so an implicit function group such as
     * `sin(30` reads back as `sin(30)`.
     */
    fun pretty(tokens: List<Tok>): String {
        val sb = StringBuilder()
        var open = 0
        for (token in tokens) {
            when (token) {
                is Tok.Num -> {
                    // A value that came from a function is logged as it was written, e.g.
                    // `sin(60)`, so the trail explains where the number came from.
                    sb.append(token.origin ?: NumberFormat.group(token.text))
                    if (token.percent) sb.append('%')
                }
                is Tok.Op -> {
                    if (sb.isNotEmpty() && !sb.last().isWhitespace()) sb.append(' ')
                    sb.append(
                        when (token.sym) {
                            '*' -> '×'
                            '/' -> '÷'
                            '-' -> '−'
                            else -> token.sym
                        }
                    )
                    sb.append(' ')
                }
                is Tok.Fn -> sb.append(token.name)
                Tok.LParen -> {
                    sb.append('(')
                    open++
                }
                Tok.RParen -> {
                    sb.append(')')
                    if (open > 0) open--
                }
                is Tok.Const -> sb.append(token.symbol)
            }
        }
        repeat(open) { sb.append(')') }
        return sb.toString()
    }

    const val PI_SYMBOL = "π"

    // -------------------------------------------------------------------------------------
    // Syntax tree
    // -------------------------------------------------------------------------------------

    private sealed interface Node {
        data class Number(val value: Double) : Node
        data class Negated(val child: Node) : Node
        data class Binary(val op: Char, val left: Node, val right: Node) : Node
        data class Percent(val child: Node) : Node
        data class Factorial(val child: Node) : Node
        data class Function(val name: String, val argument: Node) : Node
        data class Constant(val name: String) : Node
    }

    private fun evaluateNode(node: Node, context: EvalContext): Double = when (node) {
        is Node.Number -> node.value
        is Node.Negated -> -evaluateNode(node.child, context)
        is Node.Percent -> evaluateNode(node.child, context) / 100.0
        is Node.Factorial -> factorial(evaluateNode(node.child, context))
        is Node.Constant -> when (node.name) {
            "pi" -> kotlin.math.PI
            "e" -> kotlin.math.E
            "ans" -> context.ans
            else -> Double.NaN
        }
        is Node.Function -> {
            val argument = evaluateNode(node.argument, context)
            applyFunction(node.name, argument, context.angleMode)
        }
        is Node.Binary -> {
            val left = evaluateNode(node.left, context)
            val right = evaluateNode(node.right, context)
            when (node.op) {
                '+' -> if (node.right is Node.Percent) {
                    left + left * evaluateNode((node.right as Node.Percent).child, context) / 100.0
                } else {
                    left + right
                }
                '-' -> if (node.right is Node.Percent) {
                    left - left * evaluateNode((node.right as Node.Percent).child, context) / 100.0
                } else {
                    left - right
                }
                '*' -> left * right
                '/' -> left / right
                '^' -> left.pow(right)
                else -> Double.NaN
            }
        }
    }

    private fun applyFunction(name: String, argument: Double, angleMode: AngleMode): Double {
        val radians = if (angleMode == AngleMode.DEG) argument * kotlin.math.PI / 180.0 else argument
        return when (name.lowercase()) {
            "sin" -> sin(radians)
            "cos" -> cos(radians)
            "tan" -> tan(radians)
            "log" -> log10(argument)
            "ln" -> ln(argument)
            "sqrt" -> sqrt(argument)
            "abs" -> abs(argument)
            "fact" -> factorial(argument)
            else -> Double.NaN
        }
    }

    private fun factorial(value: Double): Double {
        if (value.isNaN() || value < 0.0) return Double.NaN
        val n = value.toLong()
        if (n.toDouble() != value) return Double.NaN
        if (n > 170) return Double.POSITIVE_INFINITY
        var result = 1.0
        for (i in 2..n) result *= i
        return result
    }

    // -------------------------------------------------------------------------------------
    // Parser
    // -------------------------------------------------------------------------------------

    private class Parser(source: String, private val context: EvalContext) {

        private companion object { const val END = '\u0000' }

        private val text: String = source.trim().let { trimmed ->
            val open = trimmed.count { it == '(' }
            val close = trimmed.count { it == ')' }
            val missing = (open - close).coerceAtLeast(0)
            val tail = if (trimmed.isNotEmpty() && trimmed.last() in "+-*/^") "${trimmed}0" else trimmed
            tail + ")".repeat(missing)
        }
        private var position = 0

        fun parseExpression(): Node {
            var left = parseTerm()
            while (true) {
                val op = peek()
                if (op != '+' && op != '-') return left
                position++
                val right = parseTerm()
                left = Node.Binary(op, left, right)
            }
        }

        private fun parseTerm(): Node {
            var left = parseFactor()
            while (true) {
                val op = peek()
                if (op != '*' && op != '/' && op != 'x' && op != 'X') return left
                position++
                val right = parseFactor()
                // `x` is accepted as a typed alias for the multiplication sign.
                left = Node.Binary(if (op == '/') '/' else '*', left, right)
            }
        }

        private fun parseFactor(): Node {
            val base = parseUnary()
            if (peek() == '^') {
                position++
                val exponent = parseFactor()
                return Node.Binary('^', base, exponent)
            }
            return base
        }

        private fun parseUnary(): Node {
            val op = peek()
            if (op == '-') {
                position++
                return Node.Negated(parseUnary())
            }
            if (op == '+') {
                position++
                return parseUnary()
            }
            return parsePostfix()
        }

        private fun parsePostfix(): Node {
            var node = parsePrimary()
            while (true) {
                when (peek()) {
                    '!' -> {
                        position++
                        node = Node.Factorial(node)
                    }
                    '%' -> {
                        position++
                        node = Node.Percent(node)
                    }
                    else -> return node
                }
            }
        }

        private fun parsePrimary(): Node {
            skipSpaces()
            val ch = peek()
            if (ch == END) throw ExpressionException("Unexpected end of expression")
            if (ch == '(') {
                position++
                val inner = parseExpression()
                skipSpaces()
                if (peek() != ')') throw ExpressionException("Missing closing parenthesis")
                position++
                return inner
            }
            if (ch.isDigit() || ch == '.') return parseNumber()
            if (ch.isLetter()) return parseIdentifier()
            throw ExpressionException("Unexpected character '$ch'")
        }

        private fun parseNumber(): Node {
            val start = position
            while (position < text.length && (text[position].isDigit() || text[position] == '.')) position++
            val literal = text.substring(start, position)
            val value = literal.toDoubleOrNull() ?: throw ExpressionException("Invalid number '$literal'")
            return Node.Number(value)
        }

        private fun parseIdentifier(): Node {
            val start = position
            while (position < text.length && text[position].isLetter()) position++
            val name = text.substring(start, position).lowercase()
            return when (name) {
                "pi" -> Node.Constant("pi")
                "e" -> Node.Constant("e")
                "ans" -> Node.Constant("ans")
                "sin", "cos", "tan", "log", "ln", "sqrt", "abs", "fact" -> {
                    skipSpaces()
                    if (peek() != '(') throw ExpressionException("Function '$name' requires an argument")
                    position++
                    val argument = parseExpression()
                    skipSpaces()
                    if (peek() != ')') throw ExpressionException("Missing closing parenthesis")
                    position++
                    Node.Function(name, argument)
                }
                else -> throw ExpressionException("Unknown symbol '$name'")
            }
        }

        fun expectEnd() {
            skipSpaces()
            if (position < text.length) throw ExpressionException("Unexpected trailing input")
        }

        private fun skipSpaces() {
            while (position < text.length && text[position].isWhitespace()) position++
        }

        private fun peek(): Char {
            skipSpaces()
            return if (position < text.length) text[position] else END
        }
    }
}

/** Rounds a double to the given number of decimals, avoiding locale specific formatting. */
internal fun Double.roundTo(decimals: Int): Double {
    var factor = 1.0
    repeat(decimals) { factor *= 10 }
    return (this * factor).roundToLong() / factor
}
