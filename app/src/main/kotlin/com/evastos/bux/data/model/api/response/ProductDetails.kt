package com.evastos.bux.data.model.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDetails(
    @Json(name = "symbol") val symbol: String?,
    @Json(name = "securityId") val securityId: String?,
    @Json(name = "displayName") val displayName: String?,
    @Json(name = "currentPrice") val currentPrice: ProductPrice?,
    @Json(name = "closingPrice") val closingPrice: ProductPrice?
)
