package com.evastos.bux.ui.util

import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

private const val DEBOUNCE_MILLIS = 500L

fun View.debounceClicks() =
        clicks().debounce(DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS, Schedulers.computation())