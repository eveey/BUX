package com.evastos.bux.data.exception.rtf

import android.content.Context
import com.evastos.bux.R
import com.evastos.bux.data.exception.ExceptionMessageProvider
import com.evastos.bux.inject.qualifier.AppContext

class RtfExceptionMessageProvider
constructor(@AppContext private val context: Context) : ExceptionMessageProvider<RtfException>() {

    override fun getMessage(exception: RtfException): String {
        var messageResId = R.string.rtf_error_general
        if (exception is RtfException.NotConnectedException) {
            messageResId = R.string.rtf_error_not_connected
        }
        if (exception is RtfException.NotSubscribedException) {
            messageResId = R.string.rtf_error_not_subscribed
        }
        if (exception is RtfException.NetworkException) {
            messageResId = R.string.rtf_error_network
        }
        return context.getString(messageResId)
    }

    override fun getMessage(throwable: Throwable): String {
        if (throwable is RtfException) {
            return getMessage(throwable)
        }
        return context.getString(R.string.rtf_error_general)
    }
}