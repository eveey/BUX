package com.evastos.bux.inject.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.evastos.bux.inject.qualifier.ViewModelKey
import com.evastos.bux.inject.viewmodel.ViewModelFactory
import com.evastos.bux.ui.base.BaseViewModel
import com.evastos.bux.ui.product.feed.ProductFeedViewModel
import com.evastos.bux.ui.product.identifier.ProductIdentifierViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BaseViewModel::class)
    abstract fun bindBaseViewModel(baseViewModel: BaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductIdentifierViewModel::class)
    abstract fun bindProductIdentifierViewModel(
        productIdentifierViewModel: ProductIdentifierViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductFeedViewModel::class)
    abstract fun bindProductFeedViewModel(productFeedViewModel: ProductFeedViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
