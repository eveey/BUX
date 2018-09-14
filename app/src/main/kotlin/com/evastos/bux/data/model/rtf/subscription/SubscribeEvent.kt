package com.evastos.bux.data.model.rtf.subscription

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscribeEvent(
    @Json(name = "subscribeTo") val subscribeTo: List<SubscribeChannel>,
    @Json(name = "unsubscribeFrom") val unsubscribeFrom: List<SubscribeChannel>
)
