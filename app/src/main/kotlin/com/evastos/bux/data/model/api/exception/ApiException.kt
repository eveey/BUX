package com.evastos.bux.data.model.api.exception

sealed class ApiException : Throwable() {

    class AuthException(val errorMessage: String? = null) : ApiException()

    class ServerException(val errorMessage: String? = null) : ApiException()

    class NotFoundException : ApiException()

    class NetworkException : ApiException()

    class UnknownException : ApiException()
}
