package com.evastos.bux.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import javax.inject.Inject

class HeadersInterceptor @Inject constructor() : Interceptor {

    companion object {
        private const val X_CORRELATION_ID_HEADER = "X-Correlation-Id"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestUrl = request.url().toString()

        request = request.newBuilder()
                .header(X_CORRELATION_ID_HEADER, UUID.randomUUID().toString())
                .build()

        return chain.proceed(request)
    }
}
