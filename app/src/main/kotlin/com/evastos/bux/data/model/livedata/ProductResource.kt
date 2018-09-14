package com.evastos.bux.data.model.livedata

import com.evastos.bux.data.exception.api.ApiException
import com.evastos.bux.data.model.api.response.ProductDetails

class ProductResource private constructor(
    val liveStatus: LiveStatus,
    val productDetails: ProductDetails? = null,
    val exception: ApiException? = null) {

    companion object {
        fun success(productDetails: ProductDetails): ProductResource {
            return ProductResource(liveStatus = LiveStatus.SUCCESS, productDetails = productDetails)
        }

        fun error(exception: ApiException): ProductResource {
            return ProductResource(liveStatus = LiveStatus.ERROR, exception = exception)
        }

        fun loading(): ProductResource {
            return ProductResource(liveStatus = LiveStatus.LOADING)
        }
    }
}
