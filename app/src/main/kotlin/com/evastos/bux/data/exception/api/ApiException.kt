package com.evastos.bux.data.exception.api

sealed class ApiException : Throwable() {

    class AuthException : ApiException()

    class ServerException : ApiException()

    class NotFoundException : ApiException()

    class NetworkException : ApiException()

    class UnknownException : ApiException()
}
