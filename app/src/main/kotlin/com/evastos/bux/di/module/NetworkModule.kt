package com.evastos.bux.di.module

import android.content.Context
import com.evastos.bux.BuildConfig
import com.evastos.bux.data.network.interceptor.AuthInterceptor
import com.evastos.bux.di.qualifier.AppContext
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val NETWORK_TIMEOUT_SECONDS = 30L
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message -> Timber.tag("api_call").d(message) }
                .apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        @AppContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .apply {
                    connectTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    readTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    writeTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    addInterceptor(authInterceptor)
                    addInterceptor(ChuckInterceptor(context))
                    if (BuildConfig.DEBUG) {
                        addInterceptor(loggingInterceptor)
                    }
                }.build()
    }
}
