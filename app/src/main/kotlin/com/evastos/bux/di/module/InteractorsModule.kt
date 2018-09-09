package com.evastos.bux.di.module

import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.interactor.product.ProductInteractor
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class InteractorsModule {

    @Binds
    abstract fun bindProductDataInteractor(productDataInteractor: ProductInteractor)
            : Interactors.ProductInteractor
}