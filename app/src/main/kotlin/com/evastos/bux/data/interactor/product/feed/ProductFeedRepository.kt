package com.evastos.bux.data.interactor.product.feed

import com.evastos.bux.data.interactor.Repositories
import com.evastos.bux.data.model.rtf.connection.ConnectEvent
import com.evastos.bux.data.model.rtf.subscription.SubscribeChannel
import com.evastos.bux.data.model.rtf.subscription.SubscribeEvent
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.service.RtfService
import io.reactivex.Flowable
import javax.inject.Inject

class ProductFeedRepository @Inject constructor() : Repositories.ProductFeedRepository {

    override fun connect(rtfService: RtfService): Flowable<ConnectEvent> {
        return rtfService.connect()
    }

    override fun subscribeToChannel(
        rtfService: RtfService,
        subscribeTo: String,
        unsubscribeFrom: String?
    ): Boolean {
        val subscribeList = listOf(SubscribeChannel.TradingProductChannel(subscribeTo))
        val unsubscribeList = if (unsubscribeFrom != null) {
            listOf(SubscribeChannel.TradingProductChannel(unsubscribeFrom))
        } else {
            emptyList()
        }
        return rtfService.sendSubscribe(SubscribeEvent(subscribeList, unsubscribeList))
    }

    override fun observeUpdates(rtfService: RtfService): Flowable<UpdateEvent> {
        return rtfService.observeUpdates()
    }
}