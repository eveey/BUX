package com.evastos.bux.inject.module

import com.evastos.bux.data.domain.Repositories
import com.evastos.bux.data.domain.product.details.ProductDetailsRepository
import com.evastos.bux.data.domain.product.feed.ProductFeedRepository
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
