package com.evastos.bux.inject.module

import com.evastos.bux.data.repository.Repositories
import com.evastos.bux.data.repository.product.ProductDetailsRepository
import com.evastos.bux.data.repository.product.feed.ProductFeedRepository
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindProductDetailsRepository(
        productDetailsRepository: ProductDetailsRepository): Repositories.ProductDetailsRepository

    @Binds
    abstract fun bindProductFeedRepository(
        productFeedRepository: ProductFeedRepository): Repositories.ProductFeedRepository
}
