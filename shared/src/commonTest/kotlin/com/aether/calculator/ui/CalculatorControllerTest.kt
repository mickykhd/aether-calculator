package com.aether.calculator.ui

import com.aether.calculator.core.AngleMode
import com.aether.calculator.core.UnitCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * The controller holds every piece of calculator state, so it is verified the way a user
 * would drive it: press keys, then read the display.
 */
class CalculatorControllerTest {

    private fun type(controller: CalculatorController, value: String) {
        value.forEach { ch ->
            when (ch) {
                in '0'..'9' -> controller.appendDigit(ch.toString())
                '.' -> controller.appendDecimal()
                '+' -> controller.appendOperator('+')
                '-' -> controller.appendOperator('-')
                '*' -> controller.appendOperator('*')
                '/' -> controller.appendOperator('/')
                else -> error("Unsupported key '$ch'")
            }
        }
    }

    @Test
    fun typesAndEvaluatesTheAuditTrailExample() {
        val controller = CalculatorController()
        type(controller, "1250")
        controller.appendOperator('*')
        type(controller, "1.15")

        assertEquals("1,250 ×", controller.expressionLine)
        assertEquals("1.15", controller.displayText)
        assertEquals("1,437.5", controller.livePreview)

        controller.evaluate()

        assertEquals("1,437.5", controller.displayText)
        assertEquals("1,250 × 1.15 = 1,437.5", controller.audit)
        assertEquals(1437.5, controller.ans, 1e-9)
        assertEquals(1, controller.history.size)
        assertEquals("1,437.5", controller.history.first().result)
    }

    @Test
    fun aFreshCalculatorHasNoHistoryAndNoAuditTrail() {
        val controller = CalculatorController()

        assertEquals(emptyList(), controller.history)
        assertEquals("", controller.audit)
        assertEquals("0", controller.displayText)
        assertEquals("", controller.expressionLine)
    }

    @Test
    fun theAnswerIsOnlyOfferedOnceSomethingHasBeenCalculated() {
        val controller = CalculatorController()

        assertEquals("", controller.expressionLine)

        type(controller, "7")
        controller.evaluate()

        assertEquals("Ans", controller.expressionLine)
    }

    @Test
    fun theHistoryOnlyRecordsWhatTheUserActuallyCalculated() {
        val controller = CalculatorController()

        type(controller, "480")
        controller.appendOperator('/')
        type(controller, "12")
        controller.evaluate()

        assertEquals(1, controller.history.size)
        assertEquals("480 ÷ 12", controller.history.first().expression)
        assertEquals("40", controller.history.first().result)
    }

    @Test
    fun aFunctionResultIsLoggedWithTheFunctionThatProducedIt() {
        val controller = CalculatorController()
        controller.toggleAngleMode() // degrees
        type(controller, "30")
        controller.applyFunction("sin")
        controller.evaluate()

        assertEquals("sin(30) = 0.5", controller.audit)
        assertEquals("sin(30)", controller.history.first().expression)
        assertEquals("0.5", controller.history.first().result)
    }

    @Test
    fun aFactorialIsLoggedWithItsArgument() {
        val controller = CalculatorController()
        type(controller, "5")
        controller.applyFactorial()
        controller.evaluate()

        assertEquals("5! = 120", controller.audit)
    }

    @Test
    fun aFunctionResultKeepsItsPlaceInALargerExpression() {
        val controller = CalculatorController()
        type(controller, "100")
        controller.appendOperator('*')
        type(controller, "9")
        controller.applyFunction("sqrt")
        controller.evaluate()

        assertEquals("100 × √(9) = 300", controller.audit)
    }

    @Test
    fun editingAFunctionResultForgetsHowItWasProduced() {
        val controller = CalculatorController()
        type(controller, "9")
        controller.applyFunction("sqrt")
        controller.appendDigit("5")
        controller.evaluate()

        // The user typed over the value, so the log shows the number, not the function.
        assertEquals("35 = 35", controller.audit)
    }

    @Test
    fun clearingTheLogKeepsTheCalculationInProgress() {
        val controller = CalculatorController()

        type(controller, "480")
        controller.appendOperator('/')
        type(controller, "12")
        controller.evaluate()
        controller.clearLog()

        assertEquals(emptyList(), controller.history)
        assertEquals("", controller.audit)
        // The result of the calculation is still on screen.
        assertEquals("40", controller.displayText)
    }

    @Test
    fun backspaceRemovesTheLastCharacter() {
        val controller = CalculatorController()
        type(controller, "125")
        controller.backspace()
        assertEquals("12", controller.displayText)
        controller.backspace()
        controller.backspace()
        assertEquals("0", controller.displayText)
    }

    @Test
    fun clearAllResetsTheTape() {
        val controller = CalculatorController()
        type(controller, "42")
        controller.clearAll()
        assertEquals("0", controller.displayText)
        // Nothing has been evaluated yet, so there is no answer to offer.
        assertEquals("", controller.expressionLine)
    }

    @Test
    fun replacesTheOperatorInsteadOfStackingIt() {
        val controller = CalculatorController()
        type(controller, "8")
        controller.appendOperator('+')
        controller.appendOperator('*')
        assertEquals("8 ×", controller.expressionLine)
    }

    @Test
    fun togglesParentheses() {
        val controller = CalculatorController()
        controller.toggleParentheses()
        assertTrue(controller.expressionLine.startsWith("("))
        controller.toggleParentheses()
        assertTrue(controller.expressionLine.endsWith(")"))
    }

    @Test
    fun closingAGroupKeepsWhatWasTypedInsideIt() {
        val controller = CalculatorController()
        controller.toggleParentheses()
        type(controller, "2")
        controller.appendOperator('+')
        type(controller, "3")
        controller.toggleParentheses()
        controller.appendOperator('*')
        type(controller, "4")
        controller.evaluate()

        assertEquals("(2 + 3) × 4 = 20", controller.audit)
    }

    @Test
    fun aGroupOpenedOverATypedNumberTakesItInside() {
        val controller = CalculatorController()
        type(controller, "23")
        controller.toggleParentheses()
        controller.toggleParentheses()
        controller.evaluate()

        assertEquals("(23) = 23", controller.audit)
    }

    @Test
    fun appliesPercentToTheCurrentEntry() {
        val controller = CalculatorController()
        type(controller, "10")
        controller.applyPercent()
        assertEquals("10%", controller.displayText)
    }

    @Test
    fun aDigitAfterAPercentStartsAFreshEntry() {
        val controller = CalculatorController()
        type(controller, "10")
        controller.applyPercent()
        controller.appendDigit("5")
        assertEquals("5", controller.displayText)
    }

    @Test
    fun backspaceRemovesThePercentSignFirst() {
        val controller = CalculatorController()
        type(controller, "10")
        controller.applyPercent()
        controller.backspace()
        assertEquals("10", controller.displayText)
    }

    @Test
    fun percentOfTheRunningTotalIsAddedToIt() {
        val controller = CalculatorController()
        type(controller, "200")
        controller.appendOperator('+')
        type(controller, "10")
        controller.applyPercent()
        controller.evaluate()
        assertEquals(220.0, controller.ans, 1e-9)
    }

    @Test
    fun percentOfAProductIsTakenOfTheProduct() {
        val controller = CalculatorController()
        type(controller, "200")
        controller.appendOperator('*')
        type(controller, "10")
        controller.applyPercent()
        controller.evaluate()
        assertEquals(20.0, controller.ans, 1e-9)
    }

    @Test
    fun storesTheAnswerAndReusesIt() {
        val controller = CalculatorController()
        type(controller, "21")
        controller.evaluate()
        assertEquals(21.0, controller.ans, 1e-9)

        controller.clearAll()
        type(controller, "2")
        controller.appendOperator('*')
        controller.applyConstant("ans")
        controller.evaluate()
        assertEquals(42.0, controller.ans, 1e-9)
    }

    @Test
    fun switchesAngleMode() {
        val controller = CalculatorController()
        assertEquals(AngleMode.RAD, controller.angleMode)
        controller.toggleAngleMode()
        assertEquals(AngleMode.DEG, controller.angleMode)
    }

    @Test
    fun convertsUnits() {
        val controller = CalculatorController()
        controller.selectConverterCategory(UnitCategory.LENGTH)
        assertEquals(UnitCategory.LENGTH, controller.converterCategory)
        assertEquals("0.001", controller.converterResult)

        controller.swapConverterUnits()
        assertEquals("1,000", controller.converterResult)
    }

    @Test
    fun clearsTheConverterValue() {
        val controller = CalculatorController()
        controller.clearConverter()
        controller.appendConverterDigit("4")
        controller.appendConverterDigit("2")
        assertEquals("42", controller.converterValue)
        controller.backspaceConverter()
        assertEquals("4", controller.converterValue)
        controller.clearConverter()
        assertEquals("0", controller.converterValue)
    }
}
