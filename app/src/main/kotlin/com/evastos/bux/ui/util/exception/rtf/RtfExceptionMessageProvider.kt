package com.evastos.bux.ui.util.exception.rtf

import android.content.Context
import com.evastos.bux.R
import com.evastos.bux.data.exception.rtf.RtfException
import com.evastos.bux.inject.qualifier.AppContext
import com.evastos.bux.ui.util.exception.ExceptionMessageProviders
import javax.inject.Inject

class RtfExceptionMessageProvider
@Inject constructor(@AppContext private val context: Context) : ExceptionMessageProviders.Rtf {

    override fun getMessage(exception: RtfException): String {
        var messageResId = R.string.rtf_error_general
        if (exception is RtfException.NotConnectedException) {
            messageResId = R.string.rtf_error_not_connected
        }
        if (exception is RtfException.NotSubscribedException) {
            messageResId = R.string.rtf_error_not_subscribed
        }
        if (exception is RtfException.ServerException) {
            messageResId = R.string.rtf_error_server_unavailable
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