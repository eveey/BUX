package com.evastos.bux.data.exception

interface ExceptionMapper<out E : Throwable> {

    fun map(throwable: Throwable): E
}
