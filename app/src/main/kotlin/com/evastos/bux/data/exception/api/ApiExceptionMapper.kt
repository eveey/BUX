package com.evastos.bux.data.exception.api

import com.evastos.bux.data.exception.ExceptionMapper
import com.evastos.bux.data.model.api.error.ApiError
import com.evastos.bux.data.model.api.error.ApiErrorCode.AUTH_007
import com.evastos.bux.data.model.api.error.ApiErrorCode.AUTH_008
import com.evastos.bux.data.model.api.error.ApiErrorCode.AUTH_009
import com.evastos.bux.data.model.api.error.ApiErrorCode.AUTH_014
import com.evastos.bux.data.model.api.error.ApiErrorCode.TRADING_002
import com.evastos.bux.data.exception.api.ApiException.AuthException
import com.evastos.bux.data.exception.api.ApiException.NetworkException
import com.evastos.bux.data.exception.api.ApiException.NotFoundException
import com.evastos.bux.data.exception.api.ApiException.ServerException
import com.evastos.bux.data.exception.api.ApiException.UnknownException
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

class ApiExceptionMapper(private val moshi: Moshi): ExceptionMapper<ApiException> {

    override fun map(throwable: Throwable): ApiException {
        // SocketTimeoutException or UnknownHostException
        if (throwable is IOException) {
            return NetworkException()
        }
        if (throwable is HttpException) {
            if (throwable.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                return NotFoundException()
            }
            val responseBody = throwable.response().errorBody()?.string()
            responseBody?.let { errorResponse ->
                val apiError = moshi.adapter(ApiError::class.java).fromJson(errorResponse)
                return when (apiError?.errorCode) {
                    TRADING_002 -> ServerException(apiError.message)
                    AUTH_007, AUTH_014, AUTH_009, AUTH_008 -> AuthException(apiError.message)
                    else -> UnknownException()
                }
            }
        }
        return UnknownException()
    }
}
