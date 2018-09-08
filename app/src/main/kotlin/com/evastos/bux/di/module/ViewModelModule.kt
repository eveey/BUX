package com.evastos.bux.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.evastos.bux.di.qualifier.ViewModelKey
import com.evastos.bux.di.viewmodel.ViewModelFactory
import com.evastos.bux.ui.product.ProductViewModel
import com.evastos.bux.ui.product.feed.ProductFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindProductViewModel(productViewModel: ProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductFeedViewModel::class)
    abstract fun bindProductFeedViewModel(productFeedViewModel: ProductFeedViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
