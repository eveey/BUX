package com.evastos.bux.di.module

import android.content.Context
import com.evastos.bux.BuxApp
import com.evastos.bux.data.interactor.product.feed.ProductFeedInteractor
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.service.RtfService
import com.evastos.bux.di.qualifier.AppContext
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    @AppContext
    fun provideAppContext(): Context = BuxApp.instance

    @Provides
    @Singleton
    fun provideSchedulers(): RxSchedulers {
        return RxSchedulers(Schedulers.io(),
            AndroidSchedulers.mainThread(),
            Schedulers.computation())
    }

    @Provides
    @Singleton
    fun providesProductFeedInteractor(rtfService: RtfService): ProductFeedInteractor {
        return ProductFeedInteractor(rtfService)
    }
}