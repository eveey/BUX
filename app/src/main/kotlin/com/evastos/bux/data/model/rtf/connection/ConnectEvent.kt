package com.evastos.bux.data.model.rtf.connection

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConnectEvent(
    @Json(name = "t") val eventType: ConnectEventType?,
    @Json(name = "body") val body: ConnectErrorBody?
)
