package com.evastos.bux.data.repository.product

import com.evastos.bux.data.exception.api.ApiExceptionMapper
import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.repository.Repositories
import com.evastos.bux.data.rx.mapException
import com.evastos.bux.data.service.ApiService
import io.reactivex.Single
import javax.inject.Inject

class ProductDetailsRepository @Inject constructor(
    private val apiService: ApiService,
    private val apiExceptionMapper: ApiExceptionMapper
) : Repositories.ProductDetailsRepository {

    override fun getProductDetails(productIdentifier: String): Single<ProductDetails> =
            apiService.getProductDetails(productIdentifier).mapException(apiExceptionMapper)
}
