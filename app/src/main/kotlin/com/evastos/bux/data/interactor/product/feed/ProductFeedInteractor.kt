package com.evastos.bux.data.interactor.product.feed

import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.rtf.connection.ConnectEvent
import com.evastos.bux.data.model.rtf.subscription.SubscribeChannel
import com.evastos.bux.data.model.rtf.subscription.SubscribeEvent
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.service.RtfService
import io.reactivex.Flowable
import javax.inject.Inject

class ProductFeedInteractor @Inject constructor(
    private val rtfService: RtfService
) : Interactors.ProductFeedInteractor {

    override fun connect(): Flowable<ConnectEvent> {
        return rtfService.connect()
    }

    override fun subscribeToChannel(subscribeTo: ProductId, unsubscribeFrom: ProductId?): Boolean {
        return rtfService.sendSubscribe(SubscribeEvent(listOf(SubscribeChannel.TradingProductChannel(subscribeTo)), emptyList()))
    }

    override fun observeUpdates(): Flowable<UpdateEvent> {
        return rtfService.observeUpdates()
    }
}