package com.evastos.bux.data.interactor.product.feed

import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.model.rtf.connection.ConnectEvent
import com.evastos.bux.data.model.rtf.subscription.SubscribeChannel
import com.evastos.bux.data.model.rtf.subscription.SubscribeEvent
import com.evastos.bux.data.service.RtfService
import io.reactivex.Flowable

class ProductFeedInteractor(private val rtfService: RtfService) {

    fun connect(): Flowable<ConnectEvent> {
        return rtfService.connect()
    }

    fun subscribeToChannel(subscribeTo: ProductId, unsubscribeFrom: ProductId? = null): Boolean {
        return rtfService.sendSubscribe(SubscribeEvent(listOf(SubscribeChannel.TradingProductChannel(subscribeTo)), emptyList()))
    }

    fun observeUpdates(): Flowable<UpdateEvent> {
        return rtfService.observeUpdates()
    }
}