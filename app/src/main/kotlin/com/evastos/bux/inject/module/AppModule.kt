package com.evastos.bux.inject.module

import android.app.Application
import android.content.Context
import com.evastos.bux.BuxApp
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.inject.qualifier.AppContext
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

    @Singleton
    @Provides
    fun provideApplication(): Application = BuxApp.instance

    @Provides
    @Singleton
    fun provideSchedulers(): RxSchedulers {
        return RxSchedulers(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            Schedulers.computation()
        )
    }
}
