package com.evastos.bux.data.model.rtf.subscription

import com.evastos.bux.data.model.ProductId

sealed class SubscribeChannel(val value: String) {

    class TradingProductChannel(productId: ProductId)
        : SubscribeChannel("trading.product.${productId.productId}")
}