package com.evastos.bux.data.model.rtf.subscription

import timber.log.Timber

sealed class SubscribeChannel(val value: String) {

    class TradingProductChannel(productIdentifier: String)
        : SubscribeChannel("trading.product.${productIdentifier}") {
        init {
            Timber.d(value.toString())
        }
    }
}