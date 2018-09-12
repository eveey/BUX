package com.evastos.bux.data.model.rtf.update

import com.evastos.bux.data.model.ProductId
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateEventBody(
    @Json(name = "securityId") val productId: ProductId?,
    @Json(name = "currentPrice") val currentPrice: Double?
)