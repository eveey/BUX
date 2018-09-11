package com.evastos.bux.ui.product

import com.evastos.bux.data.exception.api.ApiException
import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.product.ProductId
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.ui.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class ProductViewModel
@Inject constructor(
    private val productInteractor: Interactors.ProductInteractor,
    rxSchedulers: RxSchedulers
) : BaseViewModel(rxSchedulers) {

    fun getProductDetails(productId: ProductId) {
        disposables.add(productInteractor
                .getProductDetails(productId)
                .applySchedulers(rxSchedulers)
                .subscribe(
                    { productDetails: ProductDetails? ->
                        Timber.i(productDetails.toString())
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
