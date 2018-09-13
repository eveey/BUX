package com.evastos.bux.inject.module

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
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
    fun provideScarletBuilder(client: OkHttpClient, moshi: Moshi): Scarlet.Builder {
        val url = BuildConfig.BASE_RTF_URL + RTF_SUBSCRIPTIONS
        return Scarlet.Builder()
                .webSocketFactory(client.newWebSocketFactory(url))
                .addMessageAdapterFactory(MoshiMessageAdapter.Factory(moshi))
                .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
    }

    @Provides
    @Singleton
    fun provideLifecycle(application: Application, lifecycleOwner: LifecycleOwner): Lifecycle {
        return AndroidLifecycle.ofLifecycleOwnerForeground(application, lifecycleOwner)
    }

    @Provides
    @Singleton
    fun provideRtfService(scarlet: Scarlet): RtfService {
        return scarlet.create()
    }
}
