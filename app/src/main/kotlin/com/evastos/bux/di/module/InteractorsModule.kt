package com.evastos.bux.di.module

import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.interactor.product.data.ProductDataInteractor
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class InteractorsModule {

    @Binds
    abstract fun bindProductDataInteractor(productDataInteractor: ProductDataInteractor): Interactors.ProductDataInteractor
//
//    @Binds
//    abstract fun bindLoginInteractor(loginInteractor: LoginInteractor): Interactors.LoginInteractor

}