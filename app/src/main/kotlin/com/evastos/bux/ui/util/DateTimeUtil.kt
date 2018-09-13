package com.evastos.bux.ui.util

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

/**
 * Wrapper around date and time classes.
 */
class DateTimeUtil {

    fun getTimeNow(): String {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(LocalDateTime.now())
    }
}