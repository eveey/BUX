package com.evastos.bux.inject.module

import com.evastos.bux.data.model.TradingProducts
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
class TradingProductModule {

    @Provides
    @Singleton
    fun provideTradingProducts(): TradingProducts {
        return TradingProducts
    }
}