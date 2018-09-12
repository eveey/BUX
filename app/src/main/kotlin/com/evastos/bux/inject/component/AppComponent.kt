package com.evastos.bux.inject.component

import com.evastos.bux.BuxApp
import com.evastos.bux.inject.ActivityBuilder
import com.evastos.bux.inject.module.ApiModule
import com.evastos.bux.inject.module.AppModule
import com.evastos.bux.inject.module.RepositoryModule
import com.evastos.bux.inject.module.NetworkModule
import com.evastos.bux.inject.module.RtfModule
import com.evastos.bux.inject.module.TradingProductModule
import com.evastos.bux.inject.module.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Suppress("unused")
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBuilder::class,
    AppModule::class,
    ViewModelModule::class,
    NetworkModule::class,
    ApiModule::class,
    RtfModule::class,
    RepositoryModule::class,
    TradingProductModule::class
])
interface AppComponent : AndroidInjector<BuxApp>