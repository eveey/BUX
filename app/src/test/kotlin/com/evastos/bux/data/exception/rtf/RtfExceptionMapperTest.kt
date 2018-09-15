package com.evastos.bux.data.exception.rtf

import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Before
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RtfExceptionMapperTest {

    private lateinit var exceptionMapper: RtfExceptionMapper

    @Before
    fun setUp() {
        exceptionMapper = RtfExceptionMapper()
    }

    @Test
    fun map_withSocketTimeoutException_returnsNetworkException() {
        val throwable = SocketTimeoutException()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is RtfException.NetworkException)
    }

    @Test
    fun map_withUnknownHostException_returnsNetworkException() {
        val throwable = UnknownHostException()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is RtfException.NetworkException)
    }

    @Test
    fun map_withConnectException_returnsServerException() {
        val throwable = ConnectException()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is RtfException.ServerException)
    }

    @Test
    fun map_withOtherException_returnsUnknownException() {
        val throwable = Throwable()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is RtfException.UnknownException)
    }
}
