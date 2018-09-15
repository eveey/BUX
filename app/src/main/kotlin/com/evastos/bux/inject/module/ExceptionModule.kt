package com.evastos.bux.inject.module

import com.evastos.bux.data.exception.ExceptionMappers
import com.evastos.bux.data.exception.api.ApiExceptionMapper
import com.evastos.bux.data.exception.rtf.RtfExceptionMapper
import com.evastos.bux.ui.util.exception.ExceptionMessageProviders
import com.evastos.bux.ui.util.exception.api.ApiExceptionMessageProvider
import com.evastos.bux.ui.util.exception.rtf.RtfExceptionMessageProvider
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ExceptionModule {

    @Binds
    abstract fun bindApiExceptionMapper(
        apiExceptionMapper: ApiExceptionMapper
    ): ExceptionMappers.Api

    @Binds
    abstract fun bindRtfExceptionMapper(
        rtfExceptionMapper: RtfExceptionMapper
    ): ExceptionMappers.Rtf

    @Binds
    abstract fun bindApiExceptionMessageProvider(
        apiExceptionMessageProvider: ApiExceptionMessageProvider
    ): ExceptionMessageProviders.Api

    @Binds
    abstract fun bindRtfExceptionMessageProvider(
        rtfExceptionMessageProvider: RtfExceptionMessageProvider
    ): ExceptionMessageProviders.Rtf
}
