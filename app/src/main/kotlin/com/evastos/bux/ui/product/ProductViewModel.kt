package com.evastos.bux.ui.product

import com.evastos.bux.data.exception.api.ApiException
import com.evastos.bux.data.interactor.Repositories
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.livedata.ProductResource
import com.evastos.bux.data.model.livedata.SingleLiveEvent
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

    val productLiveData = SingleLiveEvent<ProductResource>()

    private lateinit var productDetailsRetry: () -> Unit

    fun getProductDetails(productIdentifier: String) {
        productDetailsRetry = { getProductDetails(productIdentifier) }
        productLiveData.postValue(ProductResource.loading())
        disposables.add(
            productDetailsRepository
                    .getProductDetails(productIdentifier)
                    .applySchedulers(rxSchedulers)
                    .subscribe(
                        { productDetails: ProductDetails ->
                            productLiveData.postValue(
                                ProductResource.success(productDetails))
                            Timber.i(productDetails.toString())
                        },
                        { t: Throwable ->
                            if (t is ApiException) {
                                productLiveData.postValue(
                                    ProductResource.error(t))
                            } else {
                                productLiveData.postValue(
                                    ProductResource.error(ApiException.UnknownException()))
                            }
                            Timber.e(t)
                        }
                    )
        )
    }

    fun retryGetProductDetails() {
        productDetailsRetry.invoke()
    }
}
