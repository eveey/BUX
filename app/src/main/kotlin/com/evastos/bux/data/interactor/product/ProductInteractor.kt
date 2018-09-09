package com.evastos.bux.data.interactor.product

import com.evastos.bux.data.exception.api.ApiExceptionMapper
import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.api.response.ProductData
import com.evastos.bux.data.rx.mapException
import com.evastos.bux.data.service.ApiService
import io.reactivex.Single
import javax.inject.Inject

class ProductInteractor @Inject constructor(
    private val apiService: ApiService,
    private val apiExceptionMapper: ApiExceptionMapper
) : Interactors.ProductInteractor {

    override fun execute(request: ProductId): Single<ProductData> =
            apiService.getProductData(request.productId)
                    .mapException(apiExceptionMapper)
}