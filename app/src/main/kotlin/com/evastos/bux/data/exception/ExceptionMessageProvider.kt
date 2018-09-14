package com.evastos.bux.data.exception

abstract class ExceptionMessageProvider<in Exception> {

    abstract fun getMessage(exception: Exception): String

    abstract fun getMessage(throwable: Throwable): String
}
