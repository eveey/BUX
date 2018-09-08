package com.evastos.bux.di

import com.evastos.bux.ui.product.ProductActivity
import com.evastos.bux.ui.product.feed.ProductFeedActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class DiBuilder {

    @ContributesAndroidInjector
    abstract fun bindProductActivity(): ProductActivity

    @ContributesAndroidInjector
    abstract fun bindProductFeedActivity(): ProductFeedActivity
}
