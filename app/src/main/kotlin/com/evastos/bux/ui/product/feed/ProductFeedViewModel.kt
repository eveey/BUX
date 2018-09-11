package com.evastos.bux.ui.product.feed

import com.evastos.bux.data.exception.rtf.RtfException.NotConnectedException
import com.evastos.bux.data.exception.rtf.RtfException.NotSubscribedException
import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.model.product.ProductId
import com.evastos.bux.data.model.rtf.connection.ConnectEventType.CONNECTED
import com.evastos.bux.data.model.rtf.update.Channel
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.data.rx.throttleLastMillis
import com.evastos.bux.data.service.RtfService
import com.evastos.bux.ui.base.BaseViewModel
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import io.reactivex.Flowable
import timber.log.Timber
import javax.inject.Inject

class ProductFeedViewModel @Inject constructor(
    private val productFeedInteractor: Interactors.ProductFeedInteractor,
    private val scarletBuilder: Scarlet.Builder,
    rxSchedulers: RxSchedulers
) : BaseViewModel(rxSchedulers) {

    companion object {
        private const val THROTTLE_INTERVAL = 500L
    }

    private lateinit var productId: ProductId

    private var productFeedRequest: ((ProductId) -> Unit)? = null

    fun subscribeToProductFeed(productId: ProductId, lifecycle: Lifecycle) {
        this.productId = productId
        val rtfService = scarletBuilder.lifecycle(lifecycle).build().create<RtfService>()
        subscribeToProduct(rtfService)
        productFeedRequest = { subscribeToProduct(rtfService = rtfService) }
    }

    private fun subscribeToProduct(rtfService: RtfService) {
        disposables.add(
            connect(rtfService)
                    .flatMapPublisher { connectEvent ->
                        return@flatMapPublisher when (connectEvent.eventType) {
                            CONNECTED -> {
                                productFeedInteractor.subscribeToChannel(rtfService, productId)
                                        .let { subscribed ->
                                            if (subscribed) {
                                                productFeedInteractor.observeUpdates(rtfService)
                                            } else {
                                                Flowable.error(NotSubscribedException())
                                            }
                                        }
                            }
                            else -> Flowable.error(NotConnectedException())
                        }
                    }
                    .filter {
                        it.channel == Channel.TRADING_QUOTE
                    }
                    .throttleLastMillis(THROTTLE_INTERVAL)
                    .applySchedulers(rxSchedulers)
                    .subscribe({ updateEvent ->
                        Timber.d(updateEvent.toString())
                    }, { throwable ->
                        Timber.e(throwable)
                    }))
    }

    private fun connect(rtfService: RtfService) =
            productFeedInteractor.connect(rtfService)
                    .doOnNext {
                        Timber.d(it.toString())
                    }
                    .skipWhile {
                        it.eventType == null
                    }
                    .firstOrError()
}
