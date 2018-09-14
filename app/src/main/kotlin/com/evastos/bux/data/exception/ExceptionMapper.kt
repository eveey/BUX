package com.evastos.bux.data.exception

interface ExceptionMapper<out Exception : Throwable> {

    fun map(throwable: Throwable): Exception
}
