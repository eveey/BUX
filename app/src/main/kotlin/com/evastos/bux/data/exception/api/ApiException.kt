package com.evastos.bux.data.exception.api

sealed class ApiException : Throwable() {

    class AuthException(val errorMessage: String? = null) : ApiException()

    class ServerException(val errorMessage: String? = null) : ApiException()

    class NotFoundException : ApiException()

    class NetworkException : ApiException()

    class UnknownException : ApiException()
}
