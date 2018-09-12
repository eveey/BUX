package com.evastos.bux.ui.product

import com.evastos.bux.data.exception.api.ApiException
import com.evastos.bux.data.interactor.Repositories
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.ProductId
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.ui.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class ProductViewModel
@Inject constructor(
    private val productDetailsRepository: Repositories.ProductDetailsRepository,
    rxSchedulers: RxSchedulers
) : BaseViewModel(rxSchedulers) {

    fun getProductDetails(productId: ProductId) {
        disposables.add(productDetailsRepository
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
