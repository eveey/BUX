package com.evastos.bux.data.exception.api

import android.content.Context
import com.evastos.bux.R
import com.evastos.bux.data.exception.ExceptionMessageProvider
import com.evastos.bux.inject.qualifier.AppContext

class ApiExceptionMessageProvider
constructor(@AppContext private val context: Context) : ExceptionMessageProvider<ApiException>() {

    override fun getMessage(exception: ApiException): String {
        if (exception is ApiException.NotFoundException) {
            return context.getString(R.string.api_error_product_not_found)
        }
        if (exception is ApiException.AuthException) {
            return context.getString(R.string.api_error_unauthorized)
        }
        if (exception is ApiException.ServerException) {
            return context.getString(R.string.api_error_server_unavailable)
        }
        if (exception is ApiException.NetworkException) {
            return context.getString(R.string.api_error_network)
        }
        return context.getString(R.string.api_error_general)
    }

    override fun getMessage(throwable: Throwable): String {
        if (throwable is ApiException) {
            return getMessage(throwable as ApiException)
        }
        return context.getString(R.string.api_error_general)
    }
}
