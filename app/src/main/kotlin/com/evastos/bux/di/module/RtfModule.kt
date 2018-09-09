package com.evastos.bux.di.module

import android.app.Application
import com.evastos.bux.BuildConfig
import com.evastos.bux.data.service.RtfService
import com.squareup.moshi.Moshi
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Suppress("unused")
@Module
class RtfModule {

    companion object {
        private const val RTF_SUBSCRIPTIONS = "subscriptions/me"
    }

    @Provides
    @Singleton
    fun provideScarlet(client: OkHttpClient, moshi: Moshi, lifecycle: Lifecycle): Scarlet {
        val url = BuildConfig.BASE_RTF_URL + RTF_SUBSCRIPTIONS
        return Scarlet.Builder()
                .webSocketFactory(client.newWebSocketFactory(url))
                .addMessageAdapterFactory(MoshiMessageAdapter.Factory(moshi))
                .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
                .lifecycle(lifecycle)
                .build()
    }

    @Provides
    @Singleton
    fun provideLifecycle(application: Application): Lifecycle {
        return AndroidLifecycle.ofApplicationForeground(application)
    }

    @Provides
    @Singleton
    fun provideRtfService(scarlet: Scarlet): RtfService {
        return scarlet.create()
    }
}
