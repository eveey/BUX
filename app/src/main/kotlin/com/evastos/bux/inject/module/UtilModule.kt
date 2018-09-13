package com.evastos.bux.inject.module

import com.evastos.bux.ui.util.DateTimeUtil
import com.evastos.bux.ui.util.PriceUtil
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
class UtilModule {

    @Provides
    @Singleton
    fun providesDateTimeUtil(): DateTimeUtil {
        return DateTimeUtil()
    }

    @Provides
    @Singleton
    fun providesPriceUtil(): PriceUtil {
        return PriceUtil()
    }
}
