package com.evastos.bux.ui.util

import android.support.design.widget.Snackbar
import android.view.View
import com.evastos.bux.R
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

private const val DEBOUNCE_MILLIS = 300L

fun View.debounceClicks(): Observable<Unit> =
        clicks().debounce(DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS, Schedulers.computation())

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun showSnackbar(
    view: View,
    snackbarMessage: String,
    actionMessage: String,
    action: (() -> Unit)? = null): Snackbar {
    return Snackbar.make(view, snackbarMessage, Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction(actionMessage, View.OnClickListener {
                    action?.invoke()
                })
                view.setBackgroundColor(context.getColorInt(R.color.snackbarBackground))
                show()
            }
}

fun Snackbar?.hideIfShown() {
    this?.let {
        if (it.isShown) {
            it.dismiss()
        }
    }
}
