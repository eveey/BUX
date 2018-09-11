package com.evastos.bux.di.module

import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.interactor.product.ProductDetailsInteractor
import com.evastos.bux.data.interactor.product.feed.ProductFeedInteractor
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class InteractorsModule {

    @Binds
    abstract fun bindProductInteractor(
        productDetailsInteractor: ProductDetailsInteractor): Interactors.ProductInteractor

    @Binds
    abstract fun bindProductFeedInteractor(
        productFeedInteractor: ProductFeedInteractor): Interactors.ProductFeedInteractor
}
