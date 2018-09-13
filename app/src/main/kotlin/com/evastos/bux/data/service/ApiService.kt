package com.evastos.bux.data.service

import com.evastos.bux.data.model.api.response.ProductDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("core/21/products/{identifier}")
    fun getProductDetails(@Path("identifier") productId: String): Single<ProductDetails>
}
