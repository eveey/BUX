package com.evastos.bux.inject.module

import android.content.Context
import com.evastos.bux.inject.qualifier.AppContext
import com.evastos.bux.ui.util.DateTimeUtil
import com.evastos.bux.ui.util.NumberUtil
import com.evastos.bux.ui.util.PriceUtil
import com.evastos.bux.ui.util.exception.api.ApiExceptionMessageProvider
import com.evastos.bux.ui.util.exception.rtf.RtfExceptionMessageProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
class UtilModule {

    @Provides
    @Singleton
    fun provideApiExceptionMessageProvider(
        @AppContext context: Context
    ): ApiExceptionMessageProvider {
        return ApiExceptionMessageProvider(context)
    }

    @Provides
    @Singleton
    fun provideRtfExceptionMessageProvider(
        @AppContext context: Context
    ): RtfExceptionMessageProvider {
        return RtfExceptionMessageProvider(context)
    }

    @Provides
    @Singleton
    fun providesDateTimeUtil(): DateTimeUtil {
        return DateTimeUtil()
    }

    @Provides
    @Singleton
    fun providesPriceUtil(numberUtil: NumberUtil): PriceUtil {
        return PriceUtil(numberUtil)
    }

    @Provides
    @Singleton
    fun providesNumberUtil(): NumberUtil {
        return NumberUtil()
    }
}
