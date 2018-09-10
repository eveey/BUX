package com.evastos.bux.ui.product.feed

import com.evastos.bux.data.exception.rtf.RtfException.NotConnectedException
import com.evastos.bux.data.exception.rtf.RtfException.NotSubscribedException
import com.evastos.bux.data.interactor.Interactors
import com.evastos.bux.data.manager.RtfConnectionManager
import com.evastos.bux.data.model.api.request.ProductId
import com.evastos.bux.data.model.rtf.connection.ConnectEvent
import com.evastos.bux.data.model.rtf.connection.ConnectEventType
import com.evastos.bux.data.model.rtf.update.Channel
import com.evastos.bux.data.model.rtf.update.UpdateEvent
import com.evastos.bux.data.rx.RxSchedulers
import com.evastos.bux.data.rx.applySchedulers
import com.evastos.bux.ui.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.Maybe
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProductFeedViewModel @Inject constructor(
    private val productFeedInteractor: Interactors.ProductFeedInteractor,
    private val rtfConnectionManager: RtfConnectionManager,
    rxSchedulers: RxSchedulers
) : BaseViewModel(rxSchedulers) {

    companion object {
        private const val THROTTLE_INTERVAL = 500L
    }

    private var subscribeToProductFun: (ProductId) -> Unit

    init {
        val productId = ProductId("sb26513")
        subscribeToProductFun = { subscribeToProduct(productId) }
        subscribeToProduct(productId)
    }

    private fun subscribeToProduct(productId: ProductId) {
        disposables.add(
            Maybe.concat(isConnected(), connect())
                    .firstOrError()
                    .flatMapPublisher { connectEvent ->
                        return@flatMapPublisher when (connectEvent.eventType) {
                            ConnectEventType.CONNECTED -> {
                                productFeedInteractor.subscribeToChannel(
                                    productId,
                                    rtfConnectionManager.subscribedProductId)
                                        .let { subscribed ->
                                            if (subscribed) {
                                                productFeedInteractor.observeUpdates()
                                            }
                                        }
                                Flowable.error<UpdateEvent>(NotSubscribedException())
                            }
                            else -> Flowable.error<UpdateEvent>(NotConnectedException())
                        }
                    }
                    .filter {
                        it.channel == Channel.TRADING_QUOTE
                    }
                    .throttleLast(
                        THROTTLE_INTERVAL,
                        TimeUnit.MILLISECONDS,
                        rxSchedulers.computationScheduler)
                    .applySchedulers(rxSchedulers)
                    .subscribe({ updateEvent ->
                        Timber.d(updateEvent.toString())
                    }, { throwable ->
                        Timber.e(throwable)
                    }))
    }

    private fun isConnected(): Maybe<ConnectEvent> {
        rtfConnectionManager.currentConnection?.let { currentConnection ->
            if (currentConnection.eventType == ConnectEventType.CONNECTED) {
                return Maybe.just(currentConnection)
            }
        }
        return Maybe.empty()
    }

    private fun connect(): Maybe<ConnectEvent> {
        return productFeedInteractor.connect()
                .filter {
                    it.eventType != null
                }
                .doOnNext { connectEvent ->
                    rtfConnectionManager.currentConnection = connectEvent
                    Timber.d(connectEvent.toString())
                }
                .firstElement()
    }
}
