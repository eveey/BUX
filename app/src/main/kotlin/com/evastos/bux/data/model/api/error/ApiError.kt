package com.evastos.bux.data.model.api.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * In case of an error 4xx or 5xx HTTP status code is returned.
 * The following object may be returned in the body to describe an error:
 */
@JsonClass(generateAdapter = true)
data class ApiError(
    @Json(name = "message") val message: String?,
    @Json(name = "developerMessage") val developerMessage: String?,
    @Json(name = "errorCode") val errorCode: ApiErrorCode?
)

enum class ApiErrorCode {
    @Json(name = "TRADING_002")
    TRADING_002,
    @Json(name = "AUTH_007")
    AUTH_007,
    @Json(name = "AUTH_014")
    AUTH_014,
    @Json(name = "AUTH_009")
    AUTH_009,
    @Json(name = "AUTH_008")
    AUTH_008
}