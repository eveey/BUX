package com.evastos.bux.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
) : Interceptor {

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER_TOKEN = "Bearer %s"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestUrl = request.url().toString()

        request = request.newBuilder()
                .header(AUTHORIZATION, BEARER_TOKEN.format(""))
                .build()

        return chain.proceed(request)
    }

}
