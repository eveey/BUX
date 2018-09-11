package com.evastos.bux.data.interactor.product

import com.evastos.bux.data.exception.api.ApiExceptionMapper
import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.model.product.ProductId
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.rx.mapException
import com.evastos.bux.data.service.ApiService
import io.reactivex.Single
import javax.inject.Inject

class ProductDetailsInteractor @Inject constructor(
    private val apiService: ApiService,
    private val apiExceptionMapper: ApiExceptionMapper
) : Interactors.ProductInteractor {

    override fun getProductDetails(request: ProductId): Single<ProductDetails> =
            apiService.getProductDetails(request.productId)
                    .mapException(apiExceptionMapper)
}