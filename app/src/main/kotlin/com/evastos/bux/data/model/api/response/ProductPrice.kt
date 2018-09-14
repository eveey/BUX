package com.evastos.bux.data.model.api.response

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
@JsonClass(generateAdapter = true)
data class ProductPrice(
    @Json(name = "currency") val currency: String?, // ISO 4217 currency code format
    @Json(name = "decimals") val decimals: Int?,
    @Json(name = "amount") val amount: BigDecimal?
) : Parcelable
