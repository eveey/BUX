package com.evastos.bux.ui.util

import java.math.BigDecimal

class NumberUtil {

    companion object {
        private val percentFull = 100.toBigDecimal()
    }

    fun getPercentDifference(
        previousNumber: BigDecimal,
        currentNumber: BigDecimal
    ): BigDecimal {
        return when {
            previousNumber == BigDecimal.ZERO -> percentFull
            currentNumber == BigDecimal.ZERO -> -percentFull
            currentNumber > previousNumber -> (currentNumber / previousNumber) * percentFull
            else -> -(currentNumber / previousNumber) * percentFull
        }
    }
}
