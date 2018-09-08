package com.evastos.bux.di.component

import com.evastos.bux.BuxApp
import com.evastos.bux.di.DiBuilder
import com.evastos.bux.di.module.ApiModule
import com.evastos.bux.di.module.AppModule
import com.evastos.bux.di.module.InteractorsModule
import com.evastos.bux.di.module.NetworkModule
import com.evastos.bux.di.module.UtilModule
import com.evastos.bux.di.module.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    DiBuilder::class,
    ViewModelModule::class,
    AppModule::class,
    NetworkModule::class,
    ApiModule::class,
    InteractorsModule::class,
    UtilModule::class
])
interface AppComponent : AndroidInjector<BuxApp>