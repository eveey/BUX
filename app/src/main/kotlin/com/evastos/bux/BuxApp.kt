package com.evastos.bux

import android.content.Context
import android.support.multidex.MultiDex
import com.evastos.bux.inject.component.DaggerAppComponent
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class BuxApp : DaggerApplication() {

    companion object {
        lateinit var instance: BuxApp
            private set
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.create()
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        initLogging()
        initDateTimeLibrary()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initLogging() {
//        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
//        }
    }

    private fun initDateTimeLibrary() {
        AndroidThreeTen.init(this)
    }
}
