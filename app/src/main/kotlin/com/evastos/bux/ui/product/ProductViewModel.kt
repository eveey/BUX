package com.evastos.bux.ui.product

import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.model.request.ProductId
import com.evastos.bux.data.model.response.ProductData
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.ui.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class ProductViewModel @Inject constructor(
    productDataInteractor: Interactors.ProductDataInteractor,
    rxSchedulers: RxSchedulers) : BaseViewModel() {
    init {
        compositeDisposable.add(
            productDataInteractor
                    .execute(ProductId("sb26496"))
                    .applySchedulers(rxSchedulers)
                    .subscribe({productData: ProductData? ->
                        Timber.i(productData.toString())
                    }, {t: Throwable? ->
                        Timber.e(t)
                    }))

    }
}
