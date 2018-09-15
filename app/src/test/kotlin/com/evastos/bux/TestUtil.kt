package com.evastos.bux

import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.api.response.ProductPrice
import com.evastos.bux.data.model.rtf.update.Channel
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.model.rtf.update.UpdateEventBody
import com.evastos.bux.data.rx.RxSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

object TestUtil {

    val productDetails = getProductDetails("sb27639")

    val updateEvent = UpdateEvent(
        channel = Channel.TRADING_QUOTE,
        body = UpdateEventBody(
            securityId = "sb27639",
            currentPrice = BigDecimal.valueOf(5.678)
        )
    )

    val rxSchedulers = RxSchedulers(
        Schedulers.trampoline(),
        Schedulers.trampoline(),
        Schedulers.trampoline())

    fun getProductDetails(productIdentifier: String): ProductDetails {
        return ProductDetails(
            symbol = "USD",
            securityId = productIdentifier,
            displayName = "US dollar",
            closingPrice = ProductPrice("@", 10, BigDecimal.valueOf(1.2345)),
            currentPrice = ProductPrice("$", 4, BigDecimal.valueOf(2.837))
        )
    }
}
