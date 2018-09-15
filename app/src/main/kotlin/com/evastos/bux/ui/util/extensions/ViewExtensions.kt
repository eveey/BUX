package com.evastos.bux.ui.util.extensions

import android.app.Activity
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun showSnackbarForView(
    view: View,
    snackbarMessage: String,
    actionMessage: String?,
    action: (() -> Unit)? = null): Snackbar {
    return Snackbar.make(view, snackbarMessage, Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction(actionMessage, View.OnClickListener {
                    action?.invoke()
                })
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

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}
