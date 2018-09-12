package com.evastos.bux.inject

import com.evastos.bux.ui.product.ProductActivity
import com.evastos.bux.ui.product.feed.ProductFeedActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    internal abstract fun bindProductActivity(): ProductActivity

    @ContributesAndroidInjector
    internal abstract fun bindProductFeedActivity(): ProductFeedActivity
}
