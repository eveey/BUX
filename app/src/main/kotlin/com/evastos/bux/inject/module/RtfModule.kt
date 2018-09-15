package com.evastos.bux.inject.module

import android.content.Context
import com.evastos.bux.BuildConfig
import com.evastos.bux.data.exception.rtf.RtfExceptionMapper
import com.evastos.bux.data.exception.rtf.RtfExceptionMessageProvider
import com.evastos.bux.inject.qualifier.AppContext
import com.squareup.moshi.Moshi
import com.tinder.scarlet.Scarlet
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
    fun provideScarletBuilder(okhttpClient: OkHttpClient, moshi: Moshi): Scarlet.Builder {
        val url = BuildConfig.BASE_RTF_URL + RTF_SUBSCRIPTIONS
        return Scarlet.Builder()
                .webSocketFactory(okhttpClient.newWebSocketFactory(url))
                .addMessageAdapterFactory(MoshiMessageAdapter.Factory(moshi))
                .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
    }

    @Provides
    @Singleton
    fun provideRtfExceptionMapper(): RtfExceptionMapper {
        return RtfExceptionMapper()
    }

    @Provides
    @Singleton
    fun provideRtfExceptionMessageProvider(
        @AppContext context: Context
    ): RtfExceptionMessageProvider {
        return RtfExceptionMessageProvider(context)
    }
}
