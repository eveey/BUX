package com.evastos.bux.ui.util

import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

/**
 * Wrapper around date and time classes.
 */
open class DateTimeUtil {

    /**
     * Returns formatted locale-aware date time with timezone.
     */
    fun getTimeNow(): String {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).format(ZonedDateTime.now())
    }
}
