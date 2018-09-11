package com.evastos.bux.di.component

import com.evastos.bux.BuxApp
import com.evastos.bux.di.ActivityBuilder
import com.evastos.bux.di.module.ApiModule
import com.evastos.bux.di.module.AppModule
import com.evastos.bux.di.module.InteractorsModule
import com.evastos.bux.di.module.NetworkModule
import com.evastos.bux.di.module.RtfModule
import com.evastos.bux.di.module.ViewModelModule
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
    InteractorsModule::class
])
interface AppComponent : AndroidInjector<BuxApp>