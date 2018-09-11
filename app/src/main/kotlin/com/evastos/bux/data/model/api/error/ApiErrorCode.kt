package com.evastos.bux.data.model.api.error

import com.squareup.moshi.Json

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