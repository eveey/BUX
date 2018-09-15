package com.evastos.bux.data.repository.product.feed

import com.evastos.bux.data.exception.rtf.RtfException
import com.evastos.bux.data.model.rtf.connection.ConnectEventType
import com.evastos.bux.data.model.rtf.subscription.SubscribeChannel
import com.evastos.bux.data.model.rtf.subscription.SubscribeEvent
import com.evastos.bux.data.model.rtf.update.Channel
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.repository.Repositories
import com.evastos.bux.data.service.RtfService
import io.reactivex.Flowable
import timber.log.Timber
import javax.inject.Inject

class ProductFeedRepository @Inject constructor() : Repositories.ProductFeedRepository {

    override fun subscribeToFeed(
        rtfService: RtfService,
        subscribeTo: String,
        unsubscribeFrom: String?
    ): Flowable<UpdateEvent> = connect(rtfService)
            .doOnNext { connectEvent ->
                Timber.i(connectEvent.toString())
            }
            .skipWhile {
                it.eventType == null
            }
            .firstOrError()
            .flatMapPublisher { connectEvent ->
                return@flatMapPublisher when (connectEvent.eventType) {
                    ConnectEventType.CONNECTED -> {
                        subscribeToChannel(rtfService, subscribeTo).let { subscribed ->
                            if (subscribed) {
                                observeUpdates(rtfService)
                            } else {
                                Flowable.error(RtfException.NotSubscribedException())
                            }
                        }
                    }
                    else -> Flowable.error(RtfException.NotConnectedException())
                }
            }
            .filter { updateEvent ->
                updateEvent.channel == Channel.TRADING_QUOTE
            }

    private fun connect(rtfService: RtfService) = rtfService.connect()

    private fun subscribeToChannel(
        rtfService: RtfService,
        subscribeTo: String,
        unsubscribeFrom: String? = null
    ): Boolean {
        val subscribeList = listOf(SubscribeChannel.TradingProductChannel(subscribeTo))
        val unsubscribeList = if (unsubscribeFrom != null) {
            listOf(SubscribeChannel.TradingProductChannel(unsubscribeFrom))
        } else {
            emptyList()
        }
        return rtfService.sendSubscribe(SubscribeEvent(subscribeList, unsubscribeList))
    }

    private fun observeUpdates(rtfService: RtfService) = rtfService.observeUpdates()
}
