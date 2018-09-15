package com.evastos.bux

import com.evastos.bux.data.model.api.response.ProductDetails
import com.evastos.bux.data.model.api.response.ProductPrice
import com.evastos.bux.data.model.rtf.update.Channel
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.model.rtf.update.UpdateEventBody
import com.evastos.bux.data.rx.RxSchedulers
import com.tinder.scarlet.Message
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.WebSocket
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

object TestUtil {

    val productDetails = getProductDetails("sb27639")

    val updateEvent = getUpdateEvent(Channel.TRADING_QUOTE)

    val rxSchedulers = RxSchedulers(
        Schedulers.trampoline(),
        Schedulers.trampoline(),
        Schedulers.trampoline())

    val webSocketConnectionClosing = WebSocket.Event.OnConnectionClosing(ShutdownReason.GRACEFUL)
    val webSocketConnectionFailed = WebSocket.Event.OnConnectionFailed(Throwable())
    val webSocketConnectionOpened = WebSocket.Event.OnConnectionOpened("")
    val webSocketConnectionClosed = WebSocket.Event.OnConnectionClosed(ShutdownReason.GRACEFUL)
    val webSocketMessageReceived = WebSocket.Event.OnMessageReceived(Message.Text(""))

    fun getProductDetails(productIdentifier: String): ProductDetails {
        return ProductDetails(
            symbol = "USD",
            securityId = productIdentifier,
            displayName = "US dollar",
            closingPrice = ProductPrice("@", 10, BigDecimal.valueOf(1.2345)),
            currentPrice = ProductPrice("$", 4, BigDecimal.valueOf(2.837))
        )
    }

    fun getUpdateEvent(channel: Channel): UpdateEvent {
        return UpdateEvent(channel, UpdateEventBody(
            securityId = "sb27639",
            currentPrice = BigDecimal.valueOf(5.678)
        ))
    }
}
