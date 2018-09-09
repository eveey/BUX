package com.evastos.bux.di.module

import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.interactor.product.ProductInteractor
import com.evastos.bux.data.interactor.product.feed.ProductFeedInteractor
import com.evastos.bux.data.service.RtfService
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
abstract class InteractorsModule {

    @Binds
    abstract fun bindProductDataInteractor(
        productDataInteractor: ProductInteractor): Interactors.ProductInteractor
}
