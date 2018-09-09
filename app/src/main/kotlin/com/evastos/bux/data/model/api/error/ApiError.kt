package com.evastos.bux.data.model.api.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * In case of an error 4xx or 5xx HTTP status code is returned.
 * The following object may be returned in the body to describe an error.
 */
@JsonClass(generateAdapter = true)
data class ApiError(
    @Json(name = "message") val message: String?,
    @Json(name = "developerMessage") val developerMessage: String?,
    @Json(name = "errorCode") val errorCode: ApiErrorCode?
)
