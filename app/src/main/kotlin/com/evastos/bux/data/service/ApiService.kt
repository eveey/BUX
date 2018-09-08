package com.evastos.bux.data.service

import com.evastos.bux.data.model.response.ProductData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("core/21/products/{productId}")
    fun getProductData(@Path("productId") productId: String): Single<ProductData>
}