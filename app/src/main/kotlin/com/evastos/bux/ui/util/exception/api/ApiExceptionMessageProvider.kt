package com.evastos.bux.ui.util.exception.api

import android.content.Context
import com.evastos.bux.R
import com.evastos.bux.data.exception.api.ApiException
import com.evastos.bux.inject.qualifier.AppContext
import com.evastos.bux.ui.util.exception.ExceptionMessageProviders
import javax.inject.Inject

class ApiExceptionMessageProvider
@Inject constructor(@AppContext private val context: Context) : ExceptionMessageProviders.Api {

    override fun getMessage(exception: ApiException): String {
        var messageResId = R.string.api_error_general
        if (exception is ApiException.NotFoundException) {
            messageResId = R.string.api_error_product_not_found
        }
        if (exception is ApiException.AuthException) {
            messageResId = R.string.api_error_unauthorized
        }
        if (exception is ApiException.ServerException) {
            messageResId = R.string.api_error_server_unavailable
        }
        if (exception is ApiException.NetworkException) {
            messageResId = R.string.api_error_network
        }
        return context.getString(messageResId)
    }

    override fun getMessage(throwable: Throwable): String {
        if (throwable is ApiException) {
            return getMessage(throwable)
        }
        return context.getString(R.string.api_error_general)
    }
}
