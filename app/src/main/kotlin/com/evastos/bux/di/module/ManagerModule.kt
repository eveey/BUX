package com.evastos.bux.di.module

import android.app.Application
import com.evastos.bux.BuildConfig
import com.evastos.bux.data.manager.RtfConnectionManager
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
class ManagerModule {

    @Singleton
    @Provides
    fun providesRtfConnectionManager(): RtfConnectionManager {
        return RtfConnectionManager()
    }
}
