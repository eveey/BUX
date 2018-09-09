package com.evastos.bux.ui.product

import com.evastos.bux.data.exception.api.ApiException
import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.api.response.ProductData
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.ui.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class ProductViewModel @Inject constructor(
    private val productInteractor: Interactors.ProductInteractor,
    rxSchedulers: RxSchedulers) : BaseViewModel(rxSchedulers) {
    init {
        disposables.addAll(productInteractor
                .execute(ProductId("sb26513"))
                .applySchedulers(rxSchedulers)
                .subscribe(
                    { productData: ProductData? ->
                        Timber.i(productData.toString())
                    },
                    { t: Throwable? ->
                        if (t is ApiException.AuthException) {
                            Timber.e(t.errorMessage)
                        }
                        if (t is ApiException.ServerException) {
                            Timber.e(t.errorMessage)
                        }
                        if (t is ApiException.NotFoundException) {
                            Timber.e("product not found")
                        }
                    }
                )
        )
    }

}
