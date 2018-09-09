package com.evastos.bux.di.module

import com.evastos.bux.BuildConfig
import com.evastos.bux.data.exception.api.ApiExceptionMapper
import com.evastos.bux.data.service.ApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Suppress("unused")
@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(BuildConfig.BASE_API_URL)
            client(client)
            addConverterFactory(ScalarsConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            addConverterFactory(MoshiConverterFactory.create(moshi))
        }.build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiExceptionMapper(moshi: Moshi): ApiExceptionMapper {
        return ApiExceptionMapper(moshi)
    }
}
