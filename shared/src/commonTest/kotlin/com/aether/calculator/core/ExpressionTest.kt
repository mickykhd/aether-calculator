package com.aether.calculator.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * The expression engine is the heart of the calculator, so it is verified against the
 * worked examples printed in the design system.
 */
class ExpressionTest {

    private fun eval(source: String, context: EvalContext = EvalContext()): Double =
        Expression.evaluateSource(source, context)

    @Test
    fun evaluatesTheAuditTrailExample() {
        assertEquals(1437.5, eval("1250 x 1.15"), 1e-9)
        assertEquals(1437.5, eval("1250 * 1.15"), 1e-9)
    }

    @Test
    fun evaluatesTheSeededHistoryExamples() {
        assertEquals(105.0, eval("480 / 12 + 65"), 1e-9)
        assertEquals(170.0, eval("850 x 0.20"), 1e-9)
    }

    @Test
    fun respectsOperatorPrecedence() {
        assertEquals(14.0, eval("2 + 3 x 4"), 1e-9)
        assertEquals(20.0, eval("(2 + 3) x 4"), 1e-9)
    }

    @Test
    fun powerIsRightAssociative() {
        assertEquals(512.0, eval("2 ^ 3 ^ 2"), 1e-9)
    }

    @Test
    fun handlesUnaryMinus() {
        assertEquals(-2.0, eval("-5 + 3"), 1e-9)
        assertEquals(8.0, eval("-(-8)"), 1e-9)
    }

    @Test
    fun appliesPocketCalculatorPercentSemantics() {
        // 200 + 10% is 220, ten percent of the running total.
        assertEquals(220.0, eval("200 + 10%"), 1e-9)
        // 200 x 10% is 20.
        assertEquals(20.0, eval("200 x 10%"), 1e-9)
    }

    @Test
    fun evaluatesFunctions() {
        assertEquals(4.0, eval("sqrt(16)"), 1e-9)
        assertEquals(2.0, eval("log(100)"), 1e-9)
        assertEquals(1.0, eval("ln(e)"), 1e-9)
        assertEquals(0.0, eval("sin(0)"), 1e-9)
        assertEquals(1.0, eval("cos(0)"), 1e-9)
    }

    @Test
    fun honoursTheAngleMode() {
        val degrees = EvalContext(ans = 0.0, angleMode = AngleMode.DEG)
        assertEquals(1.0, eval("sin(90)", degrees), 1e-9)
        val radians = EvalContext(ans = 0.0, angleMode = AngleMode.RAD)
        assertEquals(1.0, eval("sin(pi / 2)", radians), 1e-9)
    }

    @Test
    fun evaluatesFactorial() {
        assertEquals(120.0, eval("5!"), 1e-9)
        assertEquals(1.0, eval("0!"), 1e-9)
    }

    @Test
    fun resolvesConstantsAndAns() {
        assertEquals(kotlin.math.PI, eval("pi"), 1e-9)
        assertEquals(kotlin.math.E, eval("e"), 1e-9)
        assertEquals(43.0, eval("ans + 1", EvalContext(ans = 42.0)), 1e-9)
    }

    @Test
    fun autoClosesUnbalancedParentheses() {
        assertEquals(5.0, eval("(2 + 3"), 1e-9)
    }

    @Test
    fun treatsATrailingOperatorAsZero() {
        assertEquals(5.0, eval("5 +"), 1e-9)
    }

    @Test
    fun reportsBrokenExpressions() {
        assertFailsWith<ExpressionException> { eval("2 + )") }
        assertFailsWith<ExpressionException> { eval("foo(1)") }
    }

    @Test
    fun evaluatesATokenTape() {
        val tokens = listOf(
            Tok.Num("1250"),
            Tok.Op('*'),
            Tok.Num("1.15"),
        )
        assertEquals(1437.5, Expression.evaluate(tokens), 1e-9)
    }

    @Test
    fun rendersTheTapeWithDesignGlyphs() {
        val tokens = listOf(
            Tok.Num("1250"),
            Tok.Op('*'),
            Tok.Num("1.15"),
        )
        assertEquals("1,250 × 1.15", Expression.pretty(tokens))
        assertEquals("1250*1.15", Expression.serialize(tokens))
    }

    @Test
    fun serialisesPercentAndConstants() {
        val tokens = listOf(
            Tok.Num("10", percent = true),
            Tok.Op('+'),
            Tok.Const(Expression.PI_SYMBOL),
        )
        assertEquals("10%+pi", Expression.serialize(tokens))
        assertTrue(Expression.pretty(tokens).contains("π"))
    }

    @Test
    fun closesGroupsTheUserLeftOpen() {
        val tokens = listOf(
            Tok.Fn("sin"),
            Tok.LParen,
            Tok.Num("30"),
        )
        assertEquals("sin(30)", Expression.pretty(tokens))
    }

    @Test
    fun keepsBalancedGroupsUntouched() {
        val tokens = listOf(
            Tok.Fn("sin"),
            Tok.LParen,
            Tok.Num("30"),
            Tok.RParen,
        )
        assertEquals("sin(30)", Expression.pretty(tokens))
    }
}
