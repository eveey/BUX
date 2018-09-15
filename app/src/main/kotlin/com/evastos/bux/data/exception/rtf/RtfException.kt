package com.evastos.bux.data.exception.rtf

sealed class RtfException : Throwable() {

    class NotConnectedException : RtfException()

    class NotSubscribedException : RtfException()

    class ServerException : RtfException()

    class NetworkException : RtfException()

    class UnknownException : RtfException()
}
