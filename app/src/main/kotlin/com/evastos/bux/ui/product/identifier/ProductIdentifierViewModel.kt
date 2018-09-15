package com.evastos.bux.ui.product.identifier

import android.arch.lifecycle.MutableLiveData
import com.evastos.bux.data.exception.api.ApiExceptionMessageProvider
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.ui.livedata.SingleLiveEvent
import com.evastos.bux.data.domain.Repositories
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.ui.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class ProductIdentifierViewModel
@Inject constructor(
    private val productDetailsRepository: Repositories.ProductDetailsRepository,
    private val exceptionMessageProvider: ApiExceptionMessageProvider,
    rxSchedulers: RxSchedulers
) : BaseViewModel(rxSchedulers) {

    val productDetailsLiveData = SingleLiveEvent<ProductDetails>()
    val errorMessageLiveData = MutableLiveData<String>()
    val progressVisibleLiveData = MutableLiveData<Boolean>()

    private lateinit var productDetailsRetry: () -> Unit

    fun getProductDetails(productIdentifier: String) {
        productDetailsRetry = { getProductDetails(productIdentifier) }
        progressVisibleLiveData.postValue(true)
        disposables.clear()
        disposables.add(
            productDetailsRepository
                    .getProductDetails(productIdentifier)
                    .doFinally {
                        progressVisibleLiveData.postValue(false)
                    }
                    .applySchedulers(rxSchedulers)
                    .subscribe(
                        { productDetails: ProductDetails ->
                            productDetailsLiveData.value = productDetails
                            Timber.i(productDetails.toString())
                        },
                        { throwable: Throwable ->
                            val errorMessage = exceptionMessageProvider.getMessage(throwable)
                            errorMessageLiveData.value = errorMessage
                            Timber.e(throwable)
                        }
                    ))
    }

    fun retryGetProductDetails() {
        productDetailsRetry.invoke()
    }
}
