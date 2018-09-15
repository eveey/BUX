package com.evastos.bux.data.model.rtf.subscription

sealed class SubscribeChannel(val value: String) {

    class TradingProductChannel(productIdentifier: String)
        : SubscribeChannel("trading.product.$productIdentifier")
}
