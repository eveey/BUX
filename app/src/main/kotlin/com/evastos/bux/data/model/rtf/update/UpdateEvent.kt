package com.evastos.bux.data.model.rtf.update

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateEvent(
    @Json(name = "t") val channel: Channel,
    @Json(name = "body") val body: UpdateEventBody
)
