package com.evastos.bux.data.model.rtf.connection

import com.squareup.moshi.Json

enum class ConnectEventType {
    @Json(name = "connect.connected")
    CONNECTED,
    @Json(name = "connect.failed")
    CONNECT_FAILED
}
