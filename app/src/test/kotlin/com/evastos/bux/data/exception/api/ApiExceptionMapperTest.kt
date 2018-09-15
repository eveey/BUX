package com.evastos.bux.data.exception.api

import com.evastos.bux.data.model.api.error.ApiError
import com.evastos.bux.data.model.api.error.ApiErrorCode
import com.evastos.bux.data.network.adapter.MoshiJsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiExceptionMapperTest {

    private lateinit var exceptionMapper: ApiExceptionMapper

    private val moshi = Moshi.Builder().add(MoshiJsonAdapter()).build()

    @Before
    fun setUp() {
        exceptionMapper = ApiExceptionMapper(moshi)
    }

    @Test
    fun map_withSocketTimeoutException_returnsNetworkException() {
        val throwable = SocketTimeoutException()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is ApiException.NetworkException)
    }

    @Test
    fun map_withUnknownHostException_returnsNetworkException() {
        val throwable = UnknownHostException()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is ApiException.NetworkException)
    }

    @Test
    fun map_withConnectException_returnsServerException() {
        val throwable = ConnectException()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is ApiException.ServerException)
    }

    @Test
    fun map_withHttpExceptionNotFound_returnsProductNotFoundException() {
        val response: Response<String> = Response.error(
            HttpURLConnection.HTTP_NOT_FOUND,
            ResponseBody.create(MediaType.parse("txt/plain"),
                "")
        )

        val exception = exceptionMapper.map(HttpException(response))

        assertTrue(exception is ApiException.NotFoundException)
    }

    @Test
    fun map_withTrading002_returnsServerException() {
        val apiError = ApiError("", "", ApiErrorCode.TRADING_002)
        val response = getResponseErrorBody(apiError)

        val exception = exceptionMapper.map(HttpException(response))

        assertTrue(exception is ApiException.ServerException)
    }

    @Test
    fun map_withAuth007_returnsAuthException() {
        val apiError = ApiError("", "", ApiErrorCode.AUTH_007)
        val response = getResponseErrorBody(apiError)

        val exception = exceptionMapper.map(HttpException(response))

        assertTrue(exception is ApiException.AuthException)
    }

    @Test
    fun map_withAuth008_returnsAuthException() {
        val apiError = ApiError("", "", ApiErrorCode.AUTH_008)
        val response = getResponseErrorBody(apiError)

        val exception = exceptionMapper.map(HttpException(response))

        assertTrue(exception is ApiException.AuthException)
    }

    @Test
    fun map_withAuth009_returnsAuthException() {
        val apiError = ApiError("", "", ApiErrorCode.AUTH_009)
        val response = getResponseErrorBody(apiError)

        val exception = exceptionMapper.map(HttpException(response))

        assertTrue(exception is ApiException.AuthException)
    }

    @Test
    fun map_withAuth014_returnsAuthException() {
        val apiError = ApiError("", "", ApiErrorCode.AUTH_014)
        val response = getResponseErrorBody(apiError)

        val exception = exceptionMapper.map(HttpException(response))

        assertTrue(exception is ApiException.AuthException)
    }

    @Test
    fun map_withOtherException_returnsUnknownException() {
        val throwable = Throwable()

        val exception = exceptionMapper.map(throwable)

        assertTrue(exception is ApiException.UnknownException)
    }

    private fun getResponseErrorBody(apiError: ApiError): Response<String> {
        val request = Request.Builder().url("http://192.168.1.7").build()
        return Response.error(
            ResponseBody.create(
                MediaType.parse("txt/plain"),
                moshi.adapter(ApiError::class.java).toJson(apiError)
            ),
            okhttp3.Response.Builder().code(HttpURLConnection.HTTP_UNAVAILABLE)
                    .message("")
                    .request(request).protocol(Protocol.HTTP_1_1).build()
        )
    }
}
