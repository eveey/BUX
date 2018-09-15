package com.evastos.bux.data.exception.rtf

import com.evastos.bux.data.exception.ExceptionMapper
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RtfExceptionMapper : ExceptionMapper<RtfException> {

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
