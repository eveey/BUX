package com.evastos.bux.data.exception.rtf

import android.content.Context
import com.evastos.bux.R
import com.evastos.bux.data.exception.ExceptionMessageProvider
import com.evastos.bux.inject.qualifier.AppContext

class RtfExceptionMessageProvider
constructor(@AppContext private val context: Context) : ExceptionMessageProvider<RtfException>() {

    override fun getMessage(exception: RtfException): String {
        if (exception is RtfException.NotConnectedException) {
            return context.getString(R.string.rtf_error_not_connected)
        }
        if (exception is RtfException.NotSubscribedException) {
            return context.getString(R.string.rtf_error_not_subscribed)
        }
        if (exception is RtfException.NetworkException) {
            return context.getString(R.string.rtf_error_network)
        }
        return context.getString(R.string.rtf_error_general)
    }

    override fun getMessage(throwable: Throwable): String {
        if (throwable is RtfException) {
            return getMessage(throwable as RtfException)
        }
        return context.getString(R.string.rtf_error_general)
    }
}