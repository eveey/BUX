package com.evastos.bux.data.model.api.response

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ProductPrice(
    @Json(name = "currency") val currency: String?,
    @Json(name = "decimals") val decimals: Int?,
    @Json(name = "amount") val amount: Double?
) : Parcelable
