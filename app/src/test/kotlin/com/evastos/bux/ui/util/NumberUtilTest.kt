package com.evastos.bux.ui.util

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class NumberUtilTest {

    private lateinit var numberUtil: NumberUtil

    @Before
    fun setUp() {
        numberUtil = NumberUtil()
    }

    @Test
    fun getPercentDifference_withPositiveNumbers_shouldCalculatePositiveDifference() {
        val firstNumber = BigDecimal.valueOf(100.0)
        val secondNumber = BigDecimal.valueOf(150.0)

        val percentDifference = numberUtil.getPercentDifference(firstNumber, secondNumber)

        assertEquals(BigDecimal.valueOf(50.0), percentDifference)
    }

    @Test
    fun getPercentDifference_withPositiveNumbers_shouldCalculateNegativeDifference() {
        val firstNumber = BigDecimal.valueOf(250.0)
        val secondNumber = BigDecimal.valueOf(100.0)

        val percentDifference = numberUtil.getPercentDifference(firstNumber, secondNumber)

        assertEquals(BigDecimal.valueOf(-60.0), percentDifference)
    }

    @Test
    fun getPercentDifference_withFirstNumberZero_shouldReturnHundredPercent() {
        val firstNumber = BigDecimal.ZERO
        val secondNumber = BigDecimal.valueOf(276.0)

        val percentDifference = numberUtil.getPercentDifference(firstNumber, secondNumber)

        assertEquals(BigDecimal.valueOf(100.0), percentDifference)
    }

    @Test
    fun getPercentDifference_withSecondNumberZero_shouldReturnNegativeHundredPercent() {
        val firstNumber = BigDecimal.valueOf(276.0)
        val secondNumber = BigDecimal.ZERO

        val percentDifference = numberUtil.getPercentDifference(firstNumber, secondNumber)

        assertEquals(BigDecimal.valueOf(-100.0), percentDifference)
    }

    @Test
    fun getPercentDifference_withBothNumbersZero_shouldReturnZeroPercent() {
        val firstNumber = BigDecimal.ZERO
        val secondNumber = BigDecimal.ZERO

        val percentDifference = numberUtil.getPercentDifference(firstNumber, secondNumber)

        assertEquals(BigDecimal.valueOf(-0.0), percentDifference)
    }

    @Test
    fun getPercentDifference_withEqualNumbers_shouldReturnZeroPercent() {
        val firstNumber = BigDecimal.valueOf(276.0)
        val secondNumber = BigDecimal.valueOf(276.0)

        val percentDifference = numberUtil.getPercentDifference(firstNumber, secondNumber)

        assertEquals(BigDecimal.valueOf(-0.0), percentDifference)
    }
}
