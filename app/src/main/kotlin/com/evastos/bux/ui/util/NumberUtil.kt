package com.evastos.bux.ui.util

import java.math.BigDecimal

class NumberUtil {

    companion object {
        private const val HUNDRED = 100.0 // thanks, Detekt
        private val percentFull = 100.toBigDecimal()
    }

    fun getPercentDifference(
        previousNumber: BigDecimal,
        currentNumber: BigDecimal
    ): BigDecimal {
        return when {
            previousNumber == currentNumber -> BigDecimal.valueOf(0.0)
            previousNumber == BigDecimal.ZERO -> BigDecimal.valueOf(HUNDRED)
            else -> (currentNumber - previousNumber) / previousNumber * percentFull
        }
    }
}
