package com.evastos.bux.data.network.interceptor

import com.evastos.bux.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
) : Interceptor {

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val BEARER_TOKEN = "Bearer %s"
        private const val HEADER_ACCEPT = "Accept"
        private const val ACCEPT_JSON = "application/json"
        private const val HEADER_ACCEPT_LANGUAGE = "Accept-Language"
        private const val ACCEPT_LANGUAGE = "nl-NL,en;q=0.8"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            header(HEADER_AUTHORIZATION, BEARER_TOKEN.format(BuildConfig.AUTH_TOKEN))
            header(HEADER_ACCEPT, ACCEPT_JSON)
            header(HEADER_ACCEPT_LANGUAGE, ACCEPT_LANGUAGE)
        }.build()
        return chain.proceed(request)
    }
}
