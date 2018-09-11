package com.evastos.bux.data.model.rtf.connection

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConnectErrorBody(
    @Json(name = "developerMessage") val developerMessage: String?,
    @Json(name = "errorCode") val errorCode: String?
)

