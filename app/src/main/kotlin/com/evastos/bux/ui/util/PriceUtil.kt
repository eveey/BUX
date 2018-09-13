package com.evastos.bux.ui.util

import timber.log.Timber
import java.text.NumberFormat
import java.util.*

/**
 * Wrapper around Locale, NumberFormat and Currency classes.
 */
class PriceUtil {

    fun getLocalisedPrice(amount: Double?, currencyCode: String?, decimalPlaces: Int? = null): String {
        if (amount == null || currencyCode == null) {
            return ""
        }
        val currencyNumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val currency = getCurrency(currencyCode)
        currency?.let {
            currencyNumberFormat.currency = it
        }
        val minimumFractionDigits = decimalPlaces ?: 2
        currencyNumberFormat.minimumFractionDigits = minimumFractionDigits
        currencyNumberFormat.maximumFractionDigits = minimumFractionDigits
        return currencyNumberFormat.format(amount)
    }

    private fun getCurrency(currencyCode: String?): Currency? {
        currencyCode?.let {
            try {
                return Currency.getInstance(it)
            } catch (e: IllegalArgumentException) {
                Timber.e("Unknown currency code:$it")
            }
        }
        return null
    }
}