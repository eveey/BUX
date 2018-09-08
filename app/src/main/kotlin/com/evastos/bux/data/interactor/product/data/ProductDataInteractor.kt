package com.evastos.bux.data.interactor.product.data

import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.model.request.ProductId
import com.evastos.bux.data.service.ApiService
import javax.inject.Inject

class ProductDataInteractor @Inject constructor(
    val apiService: ApiService
) : Interactors.ProductDataInteractor {
    override fun execute(request: ProductId) = apiService.getProductData(request.productId)
}