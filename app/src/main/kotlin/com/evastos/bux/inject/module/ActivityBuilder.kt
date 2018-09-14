package com.evastos.bux.inject.module

import com.evastos.bux.ui.product.feed.ProductFeedActivity
import com.evastos.bux.ui.product.identifier.ProductIdentifierActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    internal abstract fun bindProductIdentifierActivity(): ProductIdentifierActivity

    @ContributesAndroidInjector
    internal abstract fun bindProductFeedActivity(): ProductFeedActivity
}
