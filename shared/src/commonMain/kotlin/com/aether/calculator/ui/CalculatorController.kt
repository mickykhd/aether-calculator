package com.aether.calculator.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.aether.calculator.core.AngleMode
import com.aether.calculator.core.EvalContext
import com.aether.calculator.core.Expression
import com.aether.calculator.core.ExpressionException
import com.aether.calculator.core.HistoryEntry
import com.aether.calculator.core.NumberFormat
import com.aether.calculator.core.Tok
import com.aether.calculator.core.UnitCategory
import com.aether.calculator.core.convertUnit
import com.aether.calculator.ui.theme.AetherColors
import com.aether.calculator.ui.theme.AetherPalette
import com.aether.calculator.ui.theme.AetherSettings
import com.aether.calculator.ui.theme.AetherThemes

/** The three modes exposed by the segmented control. */
enum class CalcMode { BASIC, SCIENTIFIC, CONVERTER }

/** Which side of the converter is being edited. */
enum class ConverterTarget { FROM, TO }

/**
 * Holds every piece of calculator state. The class is platform agnostic so the Android
 * and Desktop applications share exactly the same behaviour.
 */
class CalculatorController {

    var mode by mutableStateOf(CalcMode.BASIC)
    var angleMode by mutableStateOf(AngleMode.RAD)
    var tape by mutableStateOf<List<Tok>>(emptyList())
    var ans by mutableStateOf(0.0)
    var audit by mutableStateOf("")
    var history by mutableStateOf<List<HistoryEntry>>(emptyList())
    var hasError by mutableStateOf(false)

    /** True once the user has evaluated something; the answer is only then worth showing. */
    var hasResult by mutableStateOf(false)

    var showHistory by mutableStateOf(false)
    var showMenu by mutableStateOf(false)
    var showSettings by mutableStateOf(false)
    var toastMessage by mutableStateOf<String?>(null)
    var copyLabel by mutableStateOf("Copy")

    // Theme ----------------------------------------------------------------------------------

    /**
     * The key of the theme in use.
     *
     * The theme is shared by the whole app, so it is read from the palette that is on
     * screen when the controller is created.
     */
    var themeKey by mutableStateOf(AetherColors.current.key)
        private set

    /** The palette in use. */
    val theme: AetherPalette get() = AetherThemes.byKey(themeKey)

    // Unit converter ------------------------------------------------------------------------
    var converterCategory by mutableStateOf(UnitCategory.LENGTH)
    var converterFromIndex by mutableStateOf(0)
    var converterToIndex by mutableStateOf(1)
    var converterValue by mutableStateOf("1")
    var converterPicker by mutableStateOf<ConverterTarget?>(null)

    // -----------------------------------------------------------------------------------------
    // Derived display values
    // -----------------------------------------------------------------------------------------

    /** Index of the token that is currently being edited, or -1 when there is none. */
    private val entryIndex: Int
        get() {
            val last = tape.lastOrNull() ?: return -1
            return if (last is Tok.Num || last is Tok.Const) tape.lastIndex else -1
        }

    /** The literal currently being edited. */
    val entryText: String
        get() = when (val token = tape.getOrNull(entryIndex)) {
            is Tok.Num -> token.text
            is Tok.Const -> token.symbol
            else -> ""
        }

    /** True when the entry carries the percent marker. */
    private val entryIsPercent: Boolean
        get() = (tape.getOrNull(entryIndex) as? Tok.Num)?.percent == true

    /** Everything typed before the current entry, e.g. `1250 × `. */
    val expressionTokens: List<Tok>
        get() = if (entryIndex >= 0) tape.subList(0, entryIndex) else tape

    /** The primary readout. */
    val displayText: String
        get() = when {
            hasError -> "Error"
            entryText.isEmpty() -> "0"
            entryIsPercent -> NumberFormat.group(entryText) + "%"
            else -> NumberFormat.group(entryText)
        }

    /** The live result preview rendered next to the copy button. */
    val livePreview: String
        get() {
            val value = tryEvaluate() ?: return displayText
            return NumberFormat.display(value)
        }

    /** The expression line above the readout; falls back to `Ans` once a result exists. */
    val expressionLine: String
        get() {
            val prefix = Expression.pretty(expressionTokens).trim()
            return when {
                prefix.isNotEmpty() -> prefix
                hasResult -> "Ans"
                else -> ""
            }
        }

    val canEvaluate: Boolean
        get() = tape.isNotEmpty() && !hasError

    // -----------------------------------------------------------------------------------------
    // Keypad actions
    // -----------------------------------------------------------------------------------------

    fun appendDigit(digit: String) {
        clearError()
        val index = entryIndex
        if (index < 0) {
            tape = tape + Tok.Num(digit)
            return
        }
        val token = tape[index]
        if (token is Tok.Const) {
            tape = tape.dropLast(1) + Tok.Num(digit)
            return
        }
        val number = token as Tok.Num
        // A percentage is a complete token, so the next digit starts a fresh entry.
        if (number.percent) {
            tape = tape.dropLast(1) + Tok.Num(digit)
            return
        }
        val text = if (number.text == "0") digit else number.text + digit
        // Typing over a value replaces it, so the note about its origin goes away.
        tape = tape.dropLast(1) + number.copy(text = text, percent = false, origin = null)
    }

    fun appendDecimal() {
        clearError()
        val index = entryIndex
        if (index < 0) {
            tape = tape + Tok.Num("0.")
            return
        }
        val number = tape[index] as? Tok.Num ?: return
        if (number.percent) {
            tape = tape.dropLast(1) + Tok.Num("0.")
            return
        }
        if (number.text.contains('.')) return
        tape = tape.dropLast(1) + number.copy(text = number.text + ".", percent = false, origin = null)
    }

    fun appendOperator(symbol: Char) {
        clearError()
        val last = tape.lastOrNull()
        if (last is Tok.Op) {
            tape = tape.dropLast(1) + Tok.Op(symbol)
            return
        }
        val committed = if (last == null) tape + Tok.Num("0") else tape
        tape = committed + Tok.Op(symbol)
    }

    fun appendPower() {
        appendOperator('^')
    }

    /**
     * Opens a group, or closes the group that is currently open.
     *
     * A group opened while a number is being typed takes that number with it, so what the
     * user types afterwards lands inside the group. Closing a group never drops what was
     * typed inside it: `(2 + 3` stays `(2 + 3)` when the group is closed.
     */
    fun toggleParentheses() {
        clearError()
        val open = tape.count { it == Tok.LParen }
        val close = tape.count { it == Tok.RParen }
        if (open > close) {
            tape = tape + Tok.RParen
        } else if (entryIndex >= 0) {
            // The number being typed belongs inside the group that is about to open.
            tape = tape.toMutableList().apply { add(entryIndex, Tok.LParen) }
        } else {
            tape = tape + Tok.LParen
        }
    }

    fun backspace() {
        clearError()
        val index = entryIndex
        if (index >= 0) {
            val token = tape[index]
            when (token) {
                is Tok.Num -> {
                    // The percentage sign is a suffix, so it is the first thing to go.
                    if (token.percent) {
                        tape = tape.dropLast(1) + token.copy(percent = false, origin = null)
                        return
                    }
                    val shortened = token.text.dropLast(1)
                    tape = if (shortened.isEmpty()) tape.dropLast(1)
                    else tape.dropLast(1) + token.copy(text = shortened, percent = false, origin = null)
                }
                is Tok.Const -> tape = tape.dropLast(1)
                else -> Unit
            }
            return
        }
        if (tape.isNotEmpty()) tape = tape.dropLast(1)
    }

    fun clearAll() {
        hasError = false
        tape = emptyList()
    }

    fun applyPercent() {
        clearError()
        val index = entryIndex
        if (index < 0) return
        val number = tape[index] as? Tok.Num ?: return
        // The value changes here, so the note about where it came from no longer holds.
        tape = tape.dropLast(1) + number.copy(percent = true, origin = null)
    }

    fun applyFunction(name: String) {
        clearError()
        val index = entryIndex
        val number = tape.getOrNull(index) as? Tok.Num
        if (number != null && number.text.isNotEmpty()) {
            val value = number.text.toDoubleOrNull() ?: return
            val result = evaluateFunction(name, value)
            val origin = "${functionLabel(name)}(${NumberFormat.group(number.text)})"
            tape = tape.dropLast(1) + Tok.Num(NumberFormat.plain(result), origin = origin)
            return
        }
        tape = tape + Tok.Fn(name) + Tok.LParen
    }

    fun applyFactorial() {
        clearError()
        val index = entryIndex
        val number = tape.getOrNull(index) as? Tok.Num ?: return
        val value = number.text.toDoubleOrNull() ?: return
        val result = evaluateFunction("fact", value)
        val origin = "${NumberFormat.group(number.text)}!"
        tape = tape.dropLast(1) + Tok.Num(NumberFormat.plain(result), origin = origin)
    }

    fun applyConstant(symbol: String) {
        clearError()
        val index = entryIndex
        if (index < 0) {
            tape = tape + Tok.Const(symbol)
            return
        }
        tape = tape.dropLast(1) + Tok.Const(symbol)
    }

    fun evaluate() {
        clearError()
        val tapeSnapshot = tape
        if (tapeSnapshot.isEmpty()) return
        val value = tryEvaluate()
        if (value == null || value.isNaN() || value.isInfinite()) {
            hasError = true
            tape = emptyList()
            return
        }
        val expressionText = Expression.pretty(tapeSnapshot).trim()
        val resultText = NumberFormat.display(value)
        audit = "$expressionText = $resultText"
        history = listOf(HistoryEntry(expressionText, resultText, "Just now")) + history
        ans = value
        hasResult = true
        tape = listOf(Tok.Num(NumberFormat.plain(value)))
    }

    fun loadFromHistory(entry: HistoryEntry) {
        clearError()
        hasError = false
        tape = listOf(Tok.Num(entry.result.replace(",", "")))
        audit = "${entry.expression} = ${entry.result}"
        showHistory = false
        showToast("Loaded ${entry.result}")
    }

    fun clearHistory() {
        clearLog()
        showToast("History cleared")
    }

    /**
     * Empties the log and the audit trail without announcing it.
     *
     * The log belongs to the app session, not to the moment the app is in the foreground:
     * minimising the app and coming back to it leaves every calculation in place. It is
     * emptied when the user clears the history, and a fresh start of the app begins with
     * a clean sheet on its own. The number on screen and the stored answer are left
     * alone: leaving the app must not throw away work in progress.
     */
    fun clearLog() {
        history = emptyList()
        audit = ""
    }

    fun toggleAngleMode() {
        angleMode = if (angleMode == AngleMode.RAD) AngleMode.DEG else AngleMode.RAD
        showToast(if (angleMode == AngleMode.RAD) "Angle Mode: Radians" else "Angle Mode: Degrees")
    }

    fun selectMode(next: CalcMode) {
        mode = next
    }

    fun toggleScientific() {
        mode = if (mode == CalcMode.SCIENTIFIC) CalcMode.BASIC else CalcMode.SCIENTIFIC
    }

    /**
     * Switches to another theme.
     *
     * The screen cross-fades into the new colours and the choice is remembered, so the app
     * comes up in the same theme the next time it is opened.
     */
    fun selectTheme(key: String) {
        val palette = AetherThemes.byKey(key)
        themeKey = palette.key
        AetherColors.request(palette)
        AetherSettings.persist(palette.key)
        showToast("${palette.label} theme")
    }

    // -----------------------------------------------------------------------------------------
    // Unit converter
    // -----------------------------------------------------------------------------------------

    fun selectConverterCategory(category: UnitCategory) {
        converterCategory = category
        converterFromIndex = 0
        converterToIndex = category.units.size.coerceAtMost(2) - 1
        converterValue = "1"
    }

    fun setConverterUnit(index: Int, asFrom: Boolean) {
        if (asFrom) converterFromIndex = index else converterToIndex = index
    }

    fun swapConverterUnits() {
        val from = converterFromIndex
        converterFromIndex = converterToIndex
        converterToIndex = from
    }

    fun appendConverterDigit(digit: String) {
        val current = converterValue
        converterValue = if (current == "0") digit else current + digit
    }

    fun appendConverterDecimal() {
        if (!converterValue.contains('.')) {
            converterValue = if (converterValue.isEmpty()) "0." else converterValue + "."
        }
    }

    fun backspaceConverter() {
        converterValue = converterValue.dropLast(1)
    }

    fun clearConverter() {
        converterValue = "0"
    }

    /**
     * Converts the current amount.
     *
     * The result card already tracks the input live, so the action only confirms the
     * conversion; the amount in the source slot is left untouched.
     */
    fun convert(): String {
        val value = converterValue.toDoubleOrNull() ?: 0.0
        val units = converterCategory.units
        val from = units[converterFromIndex]
        val to = units[converterToIndex]
        val converted = convertUnit(value, from, to)
        val result = NumberFormat.display(converted)
        showToast("${NumberFormat.displayTyped(converterValue)} ${from.symbol} = $result ${to.symbol}")
        return result
    }

    val converterResult: String
        get() {
            val value = converterValue.toDoubleOrNull() ?: 0.0
            val units = converterCategory.units
            val converted = convertUnit(value, units[converterFromIndex], units[converterToIndex])
            return NumberFormat.display(converted)
        }

    // -----------------------------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------------------------

    fun showToast(message: String) {
        toastMessage = message
    }

    fun dismissToast() {
        toastMessage = null
    }

    private fun clearError() {
        if (hasError) {
            hasError = false
            tape = emptyList()
        }
    }

    private fun tryEvaluate(): Double? = try {
        Expression.evaluate(tape, EvalContext(ans = ans, angleMode = angleMode))
    } catch (_: ExpressionException) {
        null
    } catch (_: IllegalStateException) {
        null
    }

    private fun evaluateFunction(name: String, value: Double): Double {
        val context = EvalContext(ans = ans, angleMode = angleMode)
        val tokens = listOf(Tok.Fn(name), Tok.LParen, Tok.Num(NumberFormat.plain(value)), Tok.RParen)
        return Expression.evaluate(tokens, context)
    }

    /** The name a function is written with in the log, e.g. `√` for the square root. */
    private fun functionLabel(name: String): String = when (name.lowercase()) {
        "sqrt" -> "√"
        "fact" -> "factorial"
        else -> name.lowercase()
    }
}
