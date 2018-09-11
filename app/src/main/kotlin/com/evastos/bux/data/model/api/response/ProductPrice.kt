package com.evastos.bux.data.model.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductPrice(
    @Json(name = "currency") val currency: String?,
    @Json(name = "decimals") val decimals: Int?,
    @Json(name = "amount") val amount: Double?
)
