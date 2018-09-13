package com.evastos.bux.data.model.rtf.update

import com.squareup.moshi.Json

enum class Channel(val value: String) {
    @Json(name = "trading.quote")
    TRADING_QUOTE("trading.quote"),
    UNKNOWN("")
}