package com.evastos.bux.data.manager

import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.rtf.connection.ConnectEvent

class RtfConnectionManager {

    var currentConnection: ConnectEvent? = null

    var subscribedProductId: ProductId? = null
}