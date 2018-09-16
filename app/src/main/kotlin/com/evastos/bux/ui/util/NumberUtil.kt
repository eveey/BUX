package com.evastos.bux.ui.util

import java.math.BigDecimal
import java.math.MathContext

open class NumberUtil {

    companion object {
        private const val ONE = 1.0 // thanks, Detekt
    }

    internal open fun getPercentDifference(
        previousNumber: BigDecimal,
        currentNumber: BigDecimal
    ): BigDecimal {
        return when {
            previousNumber.compareTo(currentNumber) == 0 -> BigDecimal.valueOf(0.0)
            previousNumber.compareTo(BigDecimal.ZERO) == 0 -> BigDecimal.valueOf(ONE)
            else ->
                (currentNumber.minus(previousNumber)).divide(previousNumber, MathContext.DECIMAL64)
        }
    }
}
