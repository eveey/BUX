package com.evastos.bux.data.interactor.product.data

import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.api.response.ProductData
import com.evastos.bux.data.service.ApiService
import com.evastos.bux.data.util.ApiExceptionMapper
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class ProductDataInteractor @Inject constructor(
    private val apiService: ApiService,
    private val apiExceptionMapper: ApiExceptionMapper
) : Interactors.ProductDataInteractor {

    override fun execute(request: ProductId): Single<ProductData> =
            apiService.getProductData(request.productId)
                    .retryWhen {
                        return@retryWhen it.flatMap { throwable ->
                            Flowable.error<Throwable> {
                                apiExceptionMapper.map(throwable)
                            }
                        }
                    }
}