package com.evastos.bux.data.exception.rtf

import com.evastos.bux.data.exception.ExceptionMappers
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RtfExceptionMapper @Inject constructor() : ExceptionMappers.Rtf {

    override fun map(throwable: Throwable): RtfException {
        var exception: RtfException = RtfException.UnknownException()
        if (throwable is SocketTimeoutException || throwable is UnknownHostException) {
            exception = RtfException.NetworkException()
        }
        if (throwable is ConnectException) {
            exception = RtfException.ServerException()
        }
        return exception
    }
}
